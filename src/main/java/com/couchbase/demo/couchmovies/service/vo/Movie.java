package com.couchbase.demo.couchmovies.service.vo;

import java.util.List;

import static java.lang.String.format;


public class Movie {

    public static final String MOVIE_TYPE = "com.couchbase.demo.couchmovies.service.vo.Movie";
    public static final String MOVIE_KEY_MASK = "movie::%s";
    public List<String> genres;
    private String id;
    private String title;
    private String type;
    private Number movieId;
    private Movie() {
        setType(MOVIE_TYPE);
    }
    public Movie(Number movieId) {
        this();
        setMovieId(movieId);
        setId(format(MOVIE_KEY_MASK, movieId));
    }
    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }
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
    public Number getMovieId() {
        return movieId;
    }
    public void setMovieId(Number movieId) {
        this.movieId = movieId;
    }
}
