package com.couchbase.demo.couchmovies.data;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
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
public class SDKRepository {

    @Autowired
    Cluster cluster;

    @Value("${com.couchbase.demo.couchmovies.my-ratings-query}")
    private String myRatingsQuery;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public GetResult get(Collection collection, JsonObject o) {
        return collection.get(o.getString("key"));
    }

    public MutationResult upsert(Collection collection, JsonObject o) {
        MutationResult upsertResult = collection.upsert(
                o.getString("key"),
                o
        );

        return upsertResult;
    }

    public Flux<MutationResult> batchUpsert(ReactiveCollection collection, Flux<JsonObject> o) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchUpsert");

        return o.flatMap(
                d -> collection.upsert(d.getString("id"), d)
        ).doOnNext(fluxTracer::onNext).doOnError(fluxTracer::onError).doOnComplete(fluxTracer::onComplete);

    }

    public Iterable<JsonObject> myRatings(long userId) {

        return cluster.reactive().query(myRatingsQuery, QueryOptions.queryOptions().parameters(JsonObject.create().put("userId", userId))).flatMapMany(ReactiveQueryResult::rowsAsObject).toIterable();

    }
}
