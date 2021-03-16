package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.data.SDKRepository;
import com.couchbase.demo.couchmovies.service.vo.Rating;
import com.couchbase.demo.couchmovies.util.AsciiTable;
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
    RatingsParser ratingParser;

    @Autowired
    private SDKRepository sdkRepository;

    private Collection collection;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RatingsService(@Autowired Bucket bucket) {
        collection = bucket.defaultCollection();
    }

    @Async
    public void load(long limit, long skip, boolean random) {
        loader.load(collection.reactive(), ratingParser, this.getClass().getName(), limit, skip, random);
    }

    public void rate(long userId, long movieId, int rating) {
        Rating r = new Rating(userId, movieId, rating);
        sdkRepository.upsert(collection, ratingParser.ratingToJson(r, false));
    }

    public void myRatings(long userId) {

        Iterable<JsonObject> results  = sdkRepository.myRatings(userId);

        AsciiTable table = new AsciiTable();
        table.setMaxColumnWidth(50);
        table.getColumns().add(new AsciiTable.Column("title"));
        table.getColumns().add(new AsciiTable.Column("rating"));
        table.getColumns().add(new AsciiTable.Column("date"));


        for (JsonObject value : results) {

            AsciiTable.Row row = new AsciiTable.Row();
            table.getData().add(row);
            row.getValues().add(value.get("title").toString());
            row.getValues().add(value.get("rating").toString());
            row.getValues().add(value.get("date").toString());

        }

        table.calculateColumnWidth();
        table.render();
    }
}
