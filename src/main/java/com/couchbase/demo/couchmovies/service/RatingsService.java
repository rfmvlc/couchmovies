package com.couchbase.demo.couchmovies.service;

import com.couchbase.demo.couchmovies.data.SDKAsyncRepo;
import com.couchbase.demo.couchmovies.vo.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RatingsService {

    @Autowired
    LoaderService loader;

    @Autowired
    RatingObjectParser ratingParser;

    @Autowired
    private SDKAsyncRepo sdkAsyncRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void load(int limit) {
        loader.load(ratingParser, this.getClass().getName(), limit);
    }

    public void rate(long userId, long movieId, long rating) {
        sdkAsyncRepo.upsert(ratingParser.ratingToJson(new Rating(userId, movieId, rating)))  .subscribe(System.out::println, System.err::println);
    }
}
