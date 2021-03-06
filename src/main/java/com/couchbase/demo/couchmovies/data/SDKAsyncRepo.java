package com.couchbase.demo.couchmovies.data;

import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutateInResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.demo.couchmovies.util.FluxTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.couchbase.client.java.kv.MutateInSpec.arrayPrepend;
import static com.couchbase.client.java.kv.MutateInSpec.remove;

@Repository
public class SDKAsyncRepo {


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

    public Mono<MutationResult> upsert(ReactiveCollection collection, JsonObject o) {

        return collection.upsert(o.getString("key"), o);


    }

    public Mono<MutateInResult> upsertSubDoc(ReactiveCollection collection, String key) {

        return collection.mutateIn(key, Collections.singletonList(
                arrayPrepend("tags", Collections.singletonList(UUID.randomUUID().toString()))
        ));


    }

    public Flux<MutateInResult> batchAddTags(ReactiveCollection collection, Flux<JsonObject> o) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchAddTags");

        return o.flatMap(t -> collection
                .mutateIn(t.getString("key"),
                        Collections.singletonList(
                                arrayPrepend("tags", Collections.singletonList(t.get("tags"))).createPath()
                        )
                )
        ).onErrorContinue(new BiConsumer<Throwable, Object>() {
            @Override
            public void accept(Throwable throwable, Object o) {
                fluxTracer.onError(throwable);
            }
        }).doOnNext(fluxTracer::onNext).doOnError(fluxTracer::onError).doOnComplete(fluxTracer::onComplete);


    }

    public Flux<MutationResult> batchRemoveTags(ReactiveCollection collection, Flux<JsonObject> o) {

        FluxTracer fluxTracer = new FluxTracer(logger, "batchRemoveTags");

        return o.flatMap(t -> collection
                .mutateIn(t.getString("key"), Collections.singletonList(remove("total_tags")))
                .doOnError(fluxTracer::onError).doOnNext(fluxTracer::onNext)
        );


    }

}
