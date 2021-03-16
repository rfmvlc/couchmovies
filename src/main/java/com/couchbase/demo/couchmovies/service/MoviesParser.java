package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.service.vo.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class MoviesParser implements CsvToJsonObjectParser, MovieToJsonObjectParser {

    @Value("${com.couchbase.demo.couchmovies.moviesCsv}")
    private String moviesCsv;

    public Flux<JsonObject> parseFromCsvFile(long limit, long skip, boolean random) {
        return fromCsvFile(limit, skip).flatMap(m -> {
            Movie movie = new Movie(Long.valueOf(m[0]));
            movie.setTitle(m[1].replace("\"", ""));
            movie.setGenres(Arrays.asList(m[2].split("\\|")));
            return Flux.just(movieToJson(movie));
        });

    }

    @Override
    public String getPath() {
        return moviesCsv;
    }
}
