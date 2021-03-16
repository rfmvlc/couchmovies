package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.analytics.AnalyticsOptions;
import com.couchbase.client.java.analytics.AnalyticsResult;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.search.SearchOptions;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.MatchQuery;
import com.couchbase.client.java.search.result.ReactiveSearchResult;
import com.couchbase.client.java.search.result.SearchRow;
import com.couchbase.demo.couchmovies.util.AsciiTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.core.query.AnalyticsQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MoviesService {

    public static final String MOVIES_FTS_INDEX = "couchmovies-title-index";
    public static final String MOVIES_TITLE_FIELD = "title";
    public static final String MOVIE_KEY_PREFIX = "movie::";

    private final Cluster cluster;
    private final Collection collection;
    private final LoaderService loader;
    private final MoviesParser movieParser;

    @Value("${com.couchbase.demo.couchmovies.top-movies-query}")
    private String topMoviesQuery;

    public MoviesService(@Autowired Cluster cluster,
                         @Autowired Bucket bucket,
                         @Autowired LoaderService loader,
                         @Autowired MoviesParser movieParser) {
        this.cluster = cluster;
        this.collection = bucket.defaultCollection();
        this.loader = loader;
        this.movieParser = movieParser;
    }

    @Async
    public void load(long limit, long skip) {
        loader.load(collection.reactive(), movieParser, this.getClass().getName(), limit, skip, false);
    }

    public void search(String words) {
        MatchQuery query = SearchQuery.match(words).field(MOVIES_TITLE_FIELD);
        MatchQuery queryFuzzy = SearchQuery.match(words).field(MOVIES_TITLE_FIELD).fuzziness(1);
        SearchQuery searchQuery = SearchQuery.disjuncts(query, queryFuzzy);
        SearchOptions searchOptions = SearchOptions.searchOptions().limit(30).highlight();
        Iterable<SearchRow> results = cluster.reactive().searchQuery(MOVIES_FTS_INDEX, searchQuery, searchOptions).flatMapMany(ReactiveSearchResult::rows).toIterable();

        AsciiTable table = new AsciiTable();
        table.setMaxColumnWidth(50);
        table.getColumns().add(new AsciiTable.Column("title"));
        table.getColumns().add(new AsciiTable.Column("score"));

        for (SearchRow result : results) {
            AsciiTable.Row row = new AsciiTable.Row();
            table.getData().add(row);
            row.getValues().add(collection.get(result.id()).contentAsObject().get(MOVIES_TITLE_FIELD).toString());
            row.getValues().add(String.valueOf(result.score()));
        }

        table.calculateColumnWidth();
        table.render();
    }

    public void showTopMovies(int numMovies) {
        JsonObject queryParameters = JsonObject.create().put("numMovies", numMovies);
        AnalyticsOptions queryOptions = AnalyticsOptions.analyticsOptions().parameters(queryParameters);
        AnalyticsResult topMovies = cluster.analyticsQuery(topMoviesQuery,queryOptions);

        AsciiTable table = new AsciiTable();
        table.setMaxColumnWidth(50);
        table.getColumns().add(new AsciiTable.Column("title"));
        table.getColumns().add(new AsciiTable.Column("number of ratings"));
        table.getColumns().add(new AsciiTable.Column("average rating"));
        table.getColumns().add(new AsciiTable.Column("ranking score"));

        for (JsonObject topMovie : topMovies.rowsAsObject()) {
            long movieId = topMovie.getLong("movieId");
            long numRatings = topMovie.getLong("num_ratings");
            double averageRating = topMovie.getDouble("average_rating");
            double rankingScore = topMovie.getDouble("score");

            try {
                String topMovieTitle = collection.get(MOVIE_KEY_PREFIX + String.valueOf(movieId)).contentAsObject().get(MOVIES_TITLE_FIELD).toString();
                AsciiTable.Row row = new AsciiTable.Row();
                table.getData().add(row);
                row.getValues().add(topMovieTitle);
                row.getValues().add(String.valueOf(numRatings));
                row.getValues().add(String.valueOf(averageRating));
                row.getValues().add(String.valueOf(rankingScore));
            } catch (DocumentNotFoundException ignored) {
            }
        }

        table.calculateColumnWidth();
        table.render();
    }
}
