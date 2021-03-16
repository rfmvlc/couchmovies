package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.service.vo.Movie;

public interface MovieToJsonObjectParser {

    default JsonObject movieToJson(Movie m) {

        JsonObject movie = JsonObject.create();
        movie.put("movieId", m.getMovieId());
        movie.put("type", m.getType());
        movie.put("title", m.getTitle());
        movie.put("genres", m.getGenres());
        movie.put("id", m.getId());

        return movie;
    }
}
