package com.couchbase.demo.couchmovies.data;

import com.couchbase.demo.couchmovies.service.vo.Rating;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReactiveRatingsRepository extends ReactiveCrudRepository<Rating, String> {

}