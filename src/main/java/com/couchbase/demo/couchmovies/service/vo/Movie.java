package com.couchbase.demo.couchmovies.service.vo;

import java.util.List;

import static java.lang.String.format;

public class Movie {

    public static final String MOVIE_TYPE = "movie";
    public static final String MOVIE_KEY_MASK = "movie::%s";
    public List<String> genres;
    private String _key, title, type;
    private Number year, movieId;


    private Movie() {
        setType(MOVIE_TYPE);

    }

    public Movie(Number movieId) {
        this();
        setMovieId(movieId);
        _key = format(MOVIE_KEY_MASK, movieId);
    }


    public String getKey() {
        return _key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Number getYear() {
        return year;
    }

    public void setYear(Number year) {
        this.year = year;
    }

    public Number getMovieId() {
        return movieId;
    }

    public void setMovieId(Number movieId) {
        this.movieId = movieId;
    }
}
