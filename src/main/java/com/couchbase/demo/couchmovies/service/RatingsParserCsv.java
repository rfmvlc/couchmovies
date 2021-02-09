package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.service.vo.Rating;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class RatingsParserCsv implements CsvToJsonObjectParser {


    @Value("${com.couchbase.demo.couchmovies.ratingsCsv}")
    private String ratingsCsv;

    private Faker faker = new Faker();

    public Flux<JsonObject> parse(int limit, boolean random) {
        return fromCsvFile(limit).flatMap(r -> {
            Rating rating = new Rating(Long.valueOf(r[0]), Long.valueOf(r[1]), Float.valueOf(r[2]), Long.valueOf(r[3]) * 1000); // Timestamp Unix epoch (seconds)
            return Flux.just(ratingToJson(rating, random));
        });
    }

    public JsonObject ratingToJson(Rating r, boolean random) {

        JsonObject rating = JsonObject.create();

        rating.put("rating", random ? faker.number().numberBetween(1, 5) : r.getRating());
        rating.put("movieId", r.getMovieId());
        rating.put("userId", r.getUserId());
        rating.put("timestamp", random ? faker.date().birthday(5,10).getTime() : r.getMillis()); // 5-10 years before now
        rating.put("type", r.getType());
        rating.put("key", r.getKey());

        return rating;
    }


    @Override
    public String getPath() {
        return ratingsCsv;
    }
}
