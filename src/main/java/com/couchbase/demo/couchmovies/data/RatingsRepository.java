package com.couchbase.demo.couchmovies.data;

import com.couchbase.demo.couchmovies.service.vo.Movie;
import com.couchbase.demo.couchmovies.service.vo.Rating;
import org.springframework.data.couchbase.core.query.N1qlSecondaryIndexed;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RatingsRepository extends Repository<Rating, String> {

    List<Rating> findFirst10ByUserId(long userId);

}