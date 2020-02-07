package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.demo.couchmovies.data.SDKAsyncRepo;
import com.couchbase.demo.couchmovies.vo.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Service
@Validated
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

    public String rate(@Min(1) Number userId,
                           @Min(1) Number movieId,
                           @DecimalMin("1.0") @DecimalMax("5.0") Number rating) {

        JsonObject request = ratingParser.ratingToJson(new Rating(userId, movieId, rating));
        Long cas = sdkAsyncRepo.upsert(request).block().cas();
        GetResult result = sdkAsyncRepo.get(request);

        // CAS dumb check

        if (cas != result.cas())
            throw new IllegalStateException(String.format("CAS should match expected %s found %s", cas, result.cas()));

        return result.contentAsObject().toString();
    }
}
