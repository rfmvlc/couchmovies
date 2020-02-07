package com.couchbase.demo.couchmovies.api;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.demo.couchmovies.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Validated
@RestController
public class RatingsController {

    @Autowired
    RatingsService ratingsService;

    @GetMapping(value = "/rate/{userId}/{movieId}/{rating}", produces = "application/json")
    public String rate(@PathVariable("userId") @Min(1) Number userId,
                           @PathVariable("movieId") @Min(1) Number movieId,
                           @PathVariable("rating") @DecimalMin("1.0") @DecimalMax("5.0") Number rating) {

        return ratingsService.rate(userId, movieId, rating);
    }

}
