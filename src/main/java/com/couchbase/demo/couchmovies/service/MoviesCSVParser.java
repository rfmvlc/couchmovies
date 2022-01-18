package com.couchbase.demo.couchmovies.service;

import com.couchbase.demo.couchmovies.service.vo.Movie;
import com.couchbase.demo.couchmovies.util.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class MoviesCSVParser implements CSVReader {

    @Value("${com.couchbase.demo.couchmovies.moviesCsv}")
    private String moviesCsv;

    public Flux<Movie> parseFromCsvFile(long limit) {
        return fromCsvFile(limit).flatMap(
                m -> {
                    Movie movie = new Movie(Long.valueOf(m[0]));
                    movie.setTitle(m[1].replace("\"", ""));
                    movie.setGenres(Arrays.asList(m[2].split("\\|")));
                    return Flux.just(movie);
                });

    }

    @Override
    public String getPath() {
        return moviesCsv;
    }
}
