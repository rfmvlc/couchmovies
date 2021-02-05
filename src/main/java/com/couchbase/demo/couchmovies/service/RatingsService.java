package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.demo.couchmovies.api.dto.RatingRequest;
import com.couchbase.demo.couchmovies.data.SDKAsyncRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RatingsService {

    @Autowired
    LoaderService loader;

    @Autowired
    RatingObjectParser ratingParser;

    @Autowired
    private SDKAsyncRepo sdkAsyncRepo;

    private ReactiveCollection collection;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RatingsService(@Autowired ReactiveBucket bucket) {
        collection = bucket.collection("ratings");
    }

    @Async
    public void load(int limit) {
        loader.load(collection, ratingParser, this.getClass().getName(), limit);
    }

    public String rate(RatingRequest ratingRequest) {

        JsonObject request = ratingParser.ratingToJson(ratingRequest);
        Long cas = sdkAsyncRepo.upsert(collection, request).block().cas();
        GetResult result = sdkAsyncRepo.get(collection, request);

        // CAS dumb check

        if (cas != result.cas())
            throw new IllegalStateException(String.format("CAS should match expected %s found %s", cas, result.cas()));

        return result.contentAsObject().toString();
    }
}
