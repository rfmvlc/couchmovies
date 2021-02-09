package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.data.SDKAsyncRepo;
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
    RatingsParserCsv ratingParser;

    @Autowired
    private SDKAsyncRepo sdkAsyncRepo;

    private ReactiveCollection collection;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RatingsService(@Autowired ReactiveBucket bucket) {
        collection = bucket.scope("users").collection("ratings");
    }

    @Async
    public void load(int limit, boolean random) {
        loader.load(collection, ratingParser, this.getClass().getName(), limit, random);
    }


    public void myRatings(long userId) {

        Iterable<JsonObject> results  = sdkAsyncRepo.myRatings(userId);

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
