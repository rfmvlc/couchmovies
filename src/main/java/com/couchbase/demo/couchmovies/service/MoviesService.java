package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.analytics.AnalyticsOptions;
import com.couchbase.client.java.analytics.AnalyticsResult;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.UpsertOptions;
import com.couchbase.client.java.search.SearchOptions;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.MatchQuery;
import com.couchbase.client.java.search.result.ReactiveSearchResult;
import com.couchbase.client.java.search.result.SearchRow;
import com.couchbase.demo.couchmovies.service.vo.Movie;
import com.couchbase.demo.couchmovies.util.AsciiTable;
import com.couchbase.demo.couchmovies.util.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.couchbase.client.java.kv.UpsertOptions.upsertOptions;


@Service
public class MoviesService {

    public static final String MOVIES_FTS_INDEX = "couchmovies-title-index";
    public static final String MOVIES_TITLE_FIELD = "title";
    public static final String MOVIE_KEY_PREFIX = "movie::";

    @Autowired
    private Cluster cluster;
    @Autowired
    @Qualifier("moviesCollection")
    private Collection collection;
    @Autowired
    private MoviesCSVParser movieParser;
    @Value("${com.couchbase.demo.couchmovies.top-movies-query}")
    private String topMoviesQuery;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MoviesService() {
    }

    @Async
    public void load(long limit, boolean durability) {

        Tracer tracer = new Tracer(logger, "batchUpsert");
        UpsertOptions upsertOptions = upsertOptions();
        if (durability)
            upsertOptions.durability(DurabilityLevel.PERSIST_TO_MAJORITY);

        movieParser.parseFromCsvFile(limit)
                .flatMap(movie -> collection.reactive().upsert(movie.getId(), movie, upsertOptions))
                .doOnNext(tracer::onNext).doOnError(tracer::onError).doOnComplete(tracer::onComplete).subscribe();
    }

    public void getMovie(long movieId) {
        Movie movie = new Movie(movieId);
        System.out.println(collection.get(movie.getId()).toString());

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

    public void findTopTenMovies(int numMovies) {
        JsonObject queryParameters = JsonObject.create().put("numMovies", numMovies);
        AnalyticsOptions queryOptions = AnalyticsOptions.analyticsOptions().parameters(queryParameters);
        AnalyticsResult topMovies = cluster.analyticsQuery(topMoviesQuery, queryOptions);

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
