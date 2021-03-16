package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.service.vo.Rating;
import com.github.javafaker.Faker;

public interface RatingToJsonObjectParser {

    default JsonObject ratingToJson(Rating r, boolean random) {

        JsonObject rating = JsonObject.create();

        rating.put("rating", random ? Faker.instance().number().numberBetween(1, 5) : r.getRating());
        rating.put("movieId", r.getMovieId());
        rating.put("userId", r.getUserId());
        rating.put("timestamp", random ? Faker.instance().date().birthday(5, 10).getTime() : r.getMillis()); // 5-10 years before now
        rating.put("type", r.getType());
        rating.put("id", r.getId());

        return rating;
    }

}
