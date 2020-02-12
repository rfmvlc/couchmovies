package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.api.dto.RatingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagObjectParser implements ToJsonObjectParser {

    @Value("${com.couchbase.demo.couchmovies.tagsCsv}")
    private String tagsCsv;

    @Override
    public Flux<JsonObject> parse(int limit) {

        return fromCsvFile(limit).flatMap(
                t -> {
                    JsonObject tag = JsonObject.create();
                    // TODO: Implement parsing
                    // parsing
                    tag.put("tags", t[2]);
                    return Flux.just(tag);
                });
    }

    @Override
    public String getPath() {
        return tagsCsv;
    }
}
