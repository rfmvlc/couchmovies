package com.couchbase.demo.couchmovies.api;

import com.couchbase.demo.couchmovies.api.dto.RatingRequest;
import com.couchbase.demo.couchmovies.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class RatingsController {

    @Autowired
    RatingsService ratingsService;

    @PostMapping(value = "/rate", consumes = "application/json", produces = "application/json")
    public String rate(@Valid @RequestBody RatingRequest ratingRequest) {

        return ratingsService.rate(ratingRequest);
    }

}
