package com.couchbase.demo.couchmovies.vo;

import java.util.List;

import static java.lang.String.format;

public class Rating {

    public static final String RATING_KEY_MASK = "rating::%s::%s";
    public static final String RATING_TYPE = "rating";

    private List<String> tags;
    private Number userId, movieId, rating, timestamp;
    private String _key, type;

    private Rating() {
        setType(RATING_TYPE);
        setTimestamp(System.currentTimeMillis());
    }

    public Rating(Number userId, Number movieId, Number rating) {
        this();
        setMovieId(movieId);
        setUserId(userId);
        setRating(rating);
        _key = format(RATING_KEY_MASK, movieId, userId);

    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public Number getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Number timestamp) {
        this.timestamp = timestamp;
    }

    public String getKey() {
        return _key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
