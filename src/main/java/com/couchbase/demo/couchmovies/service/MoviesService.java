package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.ReactiveQueryResult;
import com.couchbase.client.java.search.SearchOptions;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.MatchQuery;
import com.couchbase.client.java.search.result.ReactiveSearchResult;
import com.couchbase.client.java.search.result.SearchRow;
import com.couchbase.demo.couchmovies.util.AsciiTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MoviesService {

    public static final String MOVIES_FTS_INDEX = "couchmovies-title-index";
    public static final String MOVIES_TITLE_FIELD = "title";
    public static final String MOVIE_KEY_PREFIX = "movie::";

    private final ReactiveCluster cluster;
    private final ReactiveCollection collection;
    private final LoaderService loader;
    private final MoviesParserCsv movieParser;

    @Value("${com.couchbase.demo.couchmovies.top-movies-query}")
    private String topMoviesQuery;

    public MoviesService(@Autowired ReactiveCluster cluster,
                         @Autowired ReactiveBucket bucket,
                         @Autowired LoaderService loader,
                         @Autowired MoviesParserCsv movieParser) {
        this.cluster = cluster;
        this.collection = bucket.scope("catalog").collection("movies");
        this.loader = loader;
        this.movieParser = movieParser;
    }

    @Async
    public void load(int limit) {
        loader.load(collection, movieParser, this.getClass().getName(), limit, false);
    }

    public void search(String words) {
        MatchQuery query = SearchQuery.match(words).field(MOVIES_TITLE_FIELD);
        MatchQuery queryFuzzy = SearchQuery.match(words).field(MOVIES_TITLE_FIELD).fuzziness(1);
        SearchQuery searchQuery = SearchQuery.disjuncts(query, queryFuzzy);
        SearchOptions searchOptions = SearchOptions.searchOptions().limit(30).highlight();
        Iterable<SearchRow> results = cluster.searchQuery(MOVIES_FTS_INDEX, searchQuery, searchOptions).flatMapMany(ReactiveSearchResult::rows).toIterable();

        AsciiTable table = new AsciiTable();
        table.setMaxColumnWidth(50);
        table.getColumns().add(new AsciiTable.Column("title"));
        table.getColumns().add(new AsciiTable.Column("score"));

        for (SearchRow result: results) {
            AsciiTable.Row row = new AsciiTable.Row();
            table.getData().add(row);
            row.getValues().add(collection.get(result.id()).block().contentAsObject().get(MOVIES_TITLE_FIELD).toString());
            row.getValues().add(String.valueOf(result.score()));
        }

        table.calculateColumnWidth();
        table.render();
    }

    public void showTopMovies(int numMovies) {
        Iterable<JsonObject> topMovies = cluster.query(topMoviesQuery, QueryOptions.queryOptions().parameters(JsonObject.create().put("numMovies", numMovies))).flatMapMany(ReactiveQueryResult::rowsAsObject).toIterable();

        AsciiTable table = new AsciiTable();
        table.setMaxColumnWidth(50);
        table.getColumns().add(new AsciiTable.Column("title"));
        table.getColumns().add(new AsciiTable.Column("number of ratings"));
        table.getColumns().add(new AsciiTable.Column("average rating"));
        table.getColumns().add(new AsciiTable.Column("ranking score"));

        for (JsonObject topMovie: topMovies) {
            long movieId = topMovie.getLong("movieId");
            long numRatings = topMovie.getLong("num_ratings");
            double averageRating = topMovie.getDouble("average_rating");
            double rankingScore = topMovie.getDouble("score");

            try {
                String topMovieTitle = collection.get(MOVIE_KEY_PREFIX + String.valueOf(movieId)).block().contentAsObject().get(MOVIES_TITLE_FIELD).toString();
                AsciiTable.Row row = new AsciiTable.Row();
                table.getData().add(row);
                row.getValues().add(topMovieTitle);
                row.getValues().add(String.valueOf(numRatings));
                row.getValues().add(String.valueOf(averageRating));
                row.getValues().add(String.valueOf(rankingScore));
            } catch(DocumentNotFoundException ignored) {
            }
        }

        table.calculateColumnWidth();
        table.render();
    }
}
