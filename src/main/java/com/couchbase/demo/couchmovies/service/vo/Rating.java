package com.couchbase.demo.couchmovies.service.vo;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import static java.lang.String.format;

public class Rating {

    private static final String RATING_KEY_MASK = "rating::%s::%s";
    private static final String RATING_TYPE = "com.couchbase.demo.couchmovies.service.vo.Rating";
    private @Min(1) long userId;
    private @Min(1) long movieId;
    private @Min(1) long timestamp;
    private String id;
    private String type;
    private @DecimalMin("0.5") @DecimalMax("5.0") float rating;

    private Rating() {
        setType(RATING_TYPE);
    }

    public Rating(long userId, long movieId, float rating, long timestamp) {
        this();
        setMovieId(movieId);
        setUserId(userId);
        setRating(rating);
        setTimestamp(timestamp);
        this.id = format(RATING_KEY_MASK, getMovieId(), getUserId());
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return org.apache.commons.lang3.builder.ReflectionToStringBuilder.toString(this);
    }

}
