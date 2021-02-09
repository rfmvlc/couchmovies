package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MoviesService {

    @Autowired
    LoaderService loader;

    @Autowired
    MoviesParserCsv movieParser;

    private ReactiveCollection collection;

    public MoviesService(@Autowired ReactiveBucket bucket) {
        collection = bucket.scope("catalog").collection("movies");
    }

    @Async
    public void load(int limit) {
        loader.load(collection, movieParser, this.getClass().getName(), limit, false);
    }

}
