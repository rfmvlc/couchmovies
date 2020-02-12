package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.api.dto.RatingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static java.lang.String.format;

@Component
public class RatingObjectParser implements ToJsonObjectParser {

    @Value("${com.couchbase.demo.couchmovies.ratingsCsv}")
    private String ratingsCsv;

    private static final String RATING_KEY_MASK = "rating::%s::%s";
    private static final String RATING_TYPE = "rating";

    @Override
    public Flux<JsonObject> parse(int limit) {

        return fromCsvFile(limit).flatMap(r -> {
            RatingRequest ratingRequest = new RatingRequest(Long.valueOf(r[0]), Long.valueOf(r[1]), Float.valueOf(r[2]));
            return Flux.just(ratingToJson(ratingRequest));
        });


    }

    public JsonObject ratingToJson(RatingRequest r) {

        JsonObject rating = JsonObject.create();

        rating.put("rating", r.getRating());
        rating.put("movieId", r.getMovieId());
        rating.put("userId", r.getUserId());
        rating.put("timestamp", System.currentTimeMillis());
        rating.put("type", RATING_TYPE);
        rating.put("key", format(RATING_KEY_MASK, r.getMovieId(), r.getUserId()));

        return rating;
    }


    @Override
    public String getPath() {
        return ratingsCsv;
    }
}
