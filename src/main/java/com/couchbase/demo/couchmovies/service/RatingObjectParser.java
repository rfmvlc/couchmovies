package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.vo.Rating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class RatingObjectParser implements ToJsonObjectParser {

    @Value("${com.couchbase.demo.couchmovies.ratingsCsv}")
    private String ratingsCsv;


    @Override
    public Flux<JsonObject> parse(int limit) {

        return fromCsvFile(limit).flatMap(r -> {
            Rating rating = new Rating(Long.valueOf(r[0]), Long.valueOf(r[1]), Float.valueOf(r[2]));
            rating.setTimestamp(Long.parseLong(r[3]) * 1000); // s to ms
            return Flux.just(ratingToJson(rating));
        });


    }

    JsonObject ratingToJson(Rating r) {

        JsonObject rating = JsonObject.create();

        rating.put("rating", r.getRating());
        rating.put("movieId", r.getMovieId());
        rating.put("userId", r.getUserId());
        rating.put("timestamp", r.getTimestamp());
        rating.put("type", r.getType());
        rating.put("key", r.getKey());

        return rating;
    }


    @Override
    public String getPath() {
        return ratingsCsv;
    }
}
