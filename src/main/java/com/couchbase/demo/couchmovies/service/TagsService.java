package com.couchbase.demo.couchmovies.service;


import com.couchbase.demo.couchmovies.data.SDKAsyncRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class TagsService {


    @Autowired
    SDKAsyncRepo ratingsRepository;

    @Autowired
    TagObjectParser tagParser;


    @Async
    public void addTags(int limit) {


        ratingsRepository
                .batchAddTags(
                        tagParser.parse(limit)
                )
                .subscribe();
    }


    public void removeTags() {


        ratingsRepository
                .batchRemoveTags(
                        tagParser.parse(0)
                )
                .subscribe();
    }

}
