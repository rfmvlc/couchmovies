package com.couchbase.demo.couchmovies.api.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

public class RatingRequest {

    private @Min(1) long userId;
    private @Min(1) long movieId;
    private @DecimalMin("0.5") @DecimalMax("5.0") float rating;

    public RatingRequest(long userId, long movieId, float rating) {
        setMovieId(movieId);
        setUserId(userId);
        setRating(rating);
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


}
