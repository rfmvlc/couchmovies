package com.couchbase.demo.couchmovies.api;

import com.couchbase.demo.couchmovies.dto.RatingDTO;
import com.couchbase.demo.couchmovies.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
public class RatingsController {

    @Autowired
    RatingsService ratingsService;

    @PostMapping(value = "/rate", produces = "application/json")
    public List<String> rate(@Valid @RequestBody RatingDTO dto) {

        // TODO: implement multi couchmovies rate
        return new ArrayList<>();
    }

}
