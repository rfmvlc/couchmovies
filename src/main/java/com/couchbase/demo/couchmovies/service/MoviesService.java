package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.analytics.AnalyticsOptions;
import com.couchbase.client.java.analytics.AnalyticsResult;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.search.SearchOptions;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.MatchQuery;
import com.couchbase.client.java.search.result.ReactiveSearchResult;
import com.couchbase.client.java.search.result.SearchRow;
import com.couchbase.demo.couchmovies.data.MoviesRepository;
import com.couchbase.demo.couchmovies.data.ReactiveMoviesRepository;
import com.couchbase.demo.couchmovies.service.vo.Movie;
import com.couchbase.demo.couchmovies.util.AsciiTable;
import com.couchbase.demo.couchmovies.util.FluxTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class MoviesService {

    public static final String MOVIES_FTS_INDEX = "couchmovies-title-index";
    public static final String MOVIES_TITLE_FIELD = "title";
    public static final String MOVIE_KEY_PREFIX = "movie::";
    private final Cluster cluster;
    private final Collection collection;
    private final MoviesParser movieParser;

    @Value("${com.couchbase.demo.couchmovies.top-movies-query}")
    private String topMoviesQuery;

    @Autowired
    private ReactiveMoviesRepository reactiveMoviesRepository;

    @Autowired
    private MoviesRepository moviesRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MoviesService(@Autowired Cluster cluster,
                         @Autowired Bucket bucket,
                         @Autowired MoviesParser movieParser) {
        this.cluster = cluster;
        this.collection = bucket.defaultCollection();
        this.movieParser = movieParser;
    }

    @Async
    public void load(long limit) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchUpsert");
        reactiveMoviesRepository.saveAll(movieParser.parseFromCsvFile(limit)).doOnNext(fluxTracer::onNext).doOnError(fluxTracer::onError).doOnComplete(fluxTracer::onComplete).subscribe();
    }

    @Async
    public void loadSDK(long limit) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchUpsert");
        movieParser.parseFromCsvFile(limit).
                flatMap(movie -> collection.reactive().upsert(movie.getId(), movie))
                .doOnNext(fluxTracer::onNext).doOnError(fluxTracer::onError).doOnComplete(fluxTracer::onComplete).subscribe();
    }

    public void getMovieSDK(long movieId) {
        Movie movie = new Movie(movieId);
        System.out.println(collection.get(movie.getId()).toString());

    }

    public void getMovie(long movieId) {
        Movie movie = new Movie(movieId);
        System.out.println(moviesRepository.findById(movie.getId()).get().toString());

    }

    public void findAll(int page, int pageSize, long movieId) {

        //Page<Movie> movies = moviesRepository.findAll(PageRequest.of(page, pageSize));

        Page<Movie> movies = moviesRepository.findAllByMovieIdGreaterThan(movieId, PageRequest.of(page, pageSize));

        AsciiTable table = new AsciiTable();
        table.setMaxColumnWidth(50);
        table.getColumns().add(new AsciiTable.Column("movieId"));
        table.getColumns().add(new AsciiTable.Column("title"));

        for (Movie m : movies) {

            AsciiTable.Row row = new AsciiTable.Row();
            table.getData().add(row);
            row.getValues().add(String.valueOf(m.getMovieId()));
            row.getValues().add(m.getTitle());
        }

        table.calculateColumnWidth();
        table.render();

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
