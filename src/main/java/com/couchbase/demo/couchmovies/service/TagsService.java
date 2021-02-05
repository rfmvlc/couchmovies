package com.couchbase.demo.couchmovies.service;


import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.demo.couchmovies.data.SDKAsyncRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class TagsService {


    @Autowired
    SDKAsyncRepo ratingsRepository;

    @Autowired
    TagObjectParser tagParser;

    private ReactiveCollection collection;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public TagsService(@Autowired ReactiveBucket bucket) {
        collection = bucket.collection("ratings");
    }

    @Async
    public void addTags(int limit) {


        ratingsRepository
                .batchAddTags(
                        collection,
                        tagParser.parse(limit)
                )
                .subscribe();
    }


    public void removeTags() {


        ratingsRepository
                .batchRemoveTags(
                        collection,
                        tagParser.parse(0)
                )
                .subscribe();
    }

}
