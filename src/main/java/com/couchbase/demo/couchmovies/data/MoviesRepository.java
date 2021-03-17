package com.couchbase.demo.couchmovies.data;

import com.couchbase.demo.couchmovies.service.vo.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MoviesRepository extends CrudRepository<Movie, String> {
    //Page<Movie> findAll(Pageable pageable);

    Page<Movie> findAllByMovieIdGreaterThan(long movieId, Pageable pageable);
}