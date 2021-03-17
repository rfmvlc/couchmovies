package com.couchbase.demo.couchmovies.service;

import com.couchbase.demo.couchmovies.service.vo.Rating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class RatingsParser implements FluxCsvReader {

    @Value("${com.couchbase.demo.couchmovies.ratingsCsv}")
    private String ratingsCsv;

    public Flux<Rating> parseFromCsvFile(long limit) {
        return fromCsvFile(limit).flatMap(r -> {
            Rating rating = new Rating(Long.valueOf(r[0]), Long.valueOf(r[1]), Float.valueOf(r[2]), Long.valueOf(r[3]) * 1000); // Timestamp Unix epoch (seconds)
            return Flux.just(rating);
        });
    }

    @Override
    public String getPath() {
        return ratingsCsv;
    }

}
