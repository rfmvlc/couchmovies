package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.search.SearchOptions;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.MatchQuery;
import com.couchbase.client.java.search.result.ReactiveSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MoviesService {

    public static final String MOVIES_FTS_INDEX = "couchmovies-title-index";
    public static final String MOVIES_FTS_TITLE_FIELD = "title";

    private final ReactiveCluster cluster;
    private final ReactiveCollection collection;
    private final LoaderService loader;
    private final MoviesParser movieParser;

    public MoviesService(@Autowired ReactiveCluster cluster,
                         @Autowired ReactiveBucket bucket,
                         @Autowired LoaderService loader,
                         @Autowired MoviesParser movieParser) {
        this.cluster = cluster;
        this.collection = bucket.collection("movies");
        this.loader = loader;
        this.movieParser = movieParser;
    }

    @Async
    public void load(int limit) {
        loader.load(collection, movieParser, this.getClass().getName(), limit);
    }

    public void search(String words) {
        MatchQuery query = SearchQuery.match(words).field(MOVIES_FTS_TITLE_FIELD);
        MatchQuery queryFuzzy = SearchQuery.match(words).field(MOVIES_FTS_TITLE_FIELD).fuzziness(1);
        SearchQuery searchQuery = SearchQuery.disjuncts(query, queryFuzzy);
        SearchOptions searchOptions = SearchOptions.searchOptions().limit(30).highlight();
        Mono<ReactiveSearchResult> result = cluster.searchQuery(MOVIES_FTS_INDEX, searchQuery, searchOptions);

        result
            .flatMapMany(ReactiveSearchResult::rows)
            .subscribe(row -> {
                String movieKey = row.id();
                this.collection.get(movieKey)
                        .subscribe(value -> System.out.println(value.contentAsObject().get(MOVIES_FTS_TITLE_FIELD)));
            });
    }
}
