package com.couchbase.demo.couchmovies.data;

import com.couchbase.demo.couchmovies.service.vo.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingsRepository extends CrudRepository<Rating, String> {

}