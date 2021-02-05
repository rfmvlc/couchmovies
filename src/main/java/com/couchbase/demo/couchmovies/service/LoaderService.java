package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.demo.couchmovies.data.SDKAsyncRepo;
import com.couchbase.demo.couchmovies.util.FluxTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LoaderService {


    @Autowired
    private SDKAsyncRepo sdkAsyncRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void load(ReactiveCollection collection, ToJsonObjectParser parser, String task, int limit) {

        FluxTracer fluxTracer = new FluxTracer(logger, task + "-load");

        sdkAsyncRepo
                .batchUpsert(collection,
                        parser.parse(limit)
                )
                .doOnNext(fluxTracer::onNext)
                .doOnError(fluxTracer::onError)
                .doOnComplete(fluxTracer::onComplete)
                .subscribe();

    }


}
