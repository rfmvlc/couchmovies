package com.couchbase.demo.couchmovies.data;

import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.ReactiveQueryResult;
import com.couchbase.demo.couchmovies.util.FluxTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class SDKAsyncRepo {

    @Autowired
    ReactiveCluster cluster;

    @Value("${com.couchbase.demo.couchmovies.my-ratings-query}")
    private String myRatingsQuery;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public GetResult get(ReactiveCollection collection, JsonObject o) {
        return collection.get(o.getString("key")).block();
    }

    public Flux<MutationResult> batchUpsert(ReactiveCollection collection, Flux<JsonObject> o) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchUpsert");

        return o.flatMap(
                d -> collection.upsert(d.getString("key"), d)
        ).doOnNext(fluxTracer::onNext).doOnError(fluxTracer::onError).doOnComplete(fluxTracer::onComplete);

    }

    public Iterable<JsonObject> myRatings(long userId) {

       return cluster.query(myRatingsQuery, QueryOptions.queryOptions().parameters(JsonObject.create().put("userId", userId))).flatMapMany(ReactiveQueryResult::rowsAsObject).toIterable();

    }
}
