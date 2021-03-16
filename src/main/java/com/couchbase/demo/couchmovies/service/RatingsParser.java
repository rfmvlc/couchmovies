package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.service.vo.Rating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class RatingsParser implements CsvToJsonObjectParser, RatingToJsonObjectParser {

    @Value("${com.couchbase.demo.couchmovies.ratingsCsv}")
    private String ratingsCsv;

    public Flux<JsonObject> parseFromCsvFile(long limit, long skip, boolean random) {
        return fromCsvFile(limit, skip).flatMap(r -> {
            Rating rating = new Rating(Long.valueOf(r[0]), Long.valueOf(r[1]), Float.valueOf(r[2]), Long.valueOf(r[3]) * 1000); // Timestamp Unix epoch (seconds)
            return Flux.just(ratingToJson(rating, random));
        });
    }

    @Override
    public String getPath() {
        return ratingsCsv;
    }
}
