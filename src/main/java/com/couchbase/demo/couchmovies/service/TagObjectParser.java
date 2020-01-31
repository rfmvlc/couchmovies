package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.vo.Rating;
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
                    Rating r = new Rating(Long.valueOf(t[0]), Long.valueOf(t[1]), 0);
                    List<String> tags = new ArrayList<>();
                    tags.add(t[2]);
                    r.setTags(tags);
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
