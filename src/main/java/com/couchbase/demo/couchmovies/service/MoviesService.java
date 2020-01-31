package com.couchbase.demo.couchmovies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MoviesService {

    @Autowired
    LoaderService loader;

    @Autowired
    MoviesParser movieParser;

    @Async
    public void load(int limit) {
        loader.load(movieParser, this.getClass().getName(), limit);
    }

}
