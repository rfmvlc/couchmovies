package com.couchbase.demo.couchmovies.service.vo;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import static java.lang.String.format;

public class Rating {

    private static final String RATING_KEY_MASK = "rating::%s::%s";
    private static final String RATING_TYPE = "rating";
    private @Min(1) long userId;
    private @Min(1) long movieId;
    private @Min(1) long millis;
    private String _key, type;
    private @DecimalMin("0.5") @DecimalMax("5.0") float rating;

    private Rating() {
        setType(RATING_TYPE);
    }


    public Rating(long userId, long movieId, float rating, long millis) {
        this();
        setMovieId(movieId);
        setUserId(userId);
        setRating(rating);
        setMillis(millis);
        _key = format(RATING_KEY_MASK, getMovieId(), getUserId());
    }

    public Rating(long userId, long movieId, float rating) {
        this(userId, movieId, rating, System.currentTimeMillis());
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return _key;
    }
}
