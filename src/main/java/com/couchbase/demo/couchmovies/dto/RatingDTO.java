package com.couchbase.demo.couchmovies.dto;

public class RatingDTO {

    private Number userId, movieId, rating;

    public RatingDTO() {
    }


    public Number getUserId() {
        return userId;
    }

    public void setUserId(Number userId) {
        this.userId = userId;
    }

    public Number getMovieId() {
        return movieId;
    }

    public void setMovieId(Number movieId) {
        this.movieId = movieId;
    }

    public Number getRating() {
        return rating;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }
}
