package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.service.vo.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class MoviesParserCsv implements CsvToJsonObjectParser {

    @Value("${com.couchbase.demo.couchmovies.moviesCsv}")
    private String moviesCsv;

    JsonObject movieToJson(Movie m) {

        JsonObject movie = JsonObject.create();
        movie.put("movieId", m.getMovieId());
        movie.put("type", m.getType());
        movie.put("title", m.getTitle());
        movie.put("genres", m.getGenres());
        movie.put("key", m.getKey());

        return movie;
    }

    public Flux<JsonObject> parse(int limit, boolean random) {
        return fromCsvFile(limit).flatMap(m -> {
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
