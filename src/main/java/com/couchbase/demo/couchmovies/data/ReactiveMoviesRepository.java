package com.couchbase.demo.couchmovies.data;

import com.couchbase.demo.couchmovies.service.vo.Movie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReactiveMoviesRepository extends ReactiveCrudRepository<Movie, String> {

}