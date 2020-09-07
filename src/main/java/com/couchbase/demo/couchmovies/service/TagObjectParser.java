package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.api.dto.RatingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static com.couchbase.demo.couchmovies.service.vo.Movie.MOVIE_KEY_MASK;
import static java.lang.String.format;

@Component
public class TagObjectParser implements ToJsonObjectParser {

    @Value("${com.couchbase.demo.couchmovies.tagsCsv}")
    private String tagsCsv;

    @Override
    public Flux<JsonObject> parse(int limit) {

        return fromCsvFile(limit).flatMap(
                t -> {
                    JsonObject tag = JsonObject.create();
                    tag.put("key", format(MOVIE_KEY_MASK, t[0]));
                    tag.put("tags", t[2]);
                    return Flux.just(tag);
                });
    }

    @Override
    public String getPath() {
        return tagsCsv;
    }
}
