package com.couchbase.demo.couchmovies.data;

import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.demo.couchmovies.util.FluxTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.couchbase.client.java.kv.MutateInSpec.arrayAddUnique;
import static com.couchbase.client.java.kv.MutateInSpec.remove;

@Repository
public class SDKAsyncRepo {

    @Autowired
    private ReactiveCollection collection;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public GetResult get(JsonObject o){
        return collection.get(o.getString("key")).block();
    }

    public Flux<MutationResult> batchUpsert(Flux<JsonObject> o) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchUpsert");

        return o.flatMap(
                d -> collection.upsert(d.getString("key"), d)
        ).doOnNext(fluxTracer::onNext).doOnError(fluxTracer::onError).doOnComplete(fluxTracer::onComplete);

    }

    public Mono<MutationResult> upsert(JsonObject o) {

        return collection.upsert(o.getString("key"), o);


    }

    public Flux<MutationResult> batchAddTags(Flux<JsonObject> o) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchAddTags");

        return o.flatMap(t -> collection
                .mutateIn(t.getString("key"),
                        Collections.singletonList(
                                arrayAddUnique("tags", t.get("tags"))
                                        .increment("total_tags", 1)
                        )
                ).doOnError(fluxTracer::onError).doOnNext(fluxTracer::onNext)
        );


    }

    public Flux<MutationResult> batchRemoveTags(Flux<JsonObject> o) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchRemoveTags");

        return o.flatMap(t -> collection
                .mutateIn(t.getString("key"), Collections.singletonList(remove("total_tags")))
                .doOnError(fluxTracer::onError).doOnNext(fluxTracer::onNext)
        );


    }

}
