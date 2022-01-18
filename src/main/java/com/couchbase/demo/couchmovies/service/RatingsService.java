package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.MutateInSpec;
import com.couchbase.client.java.kv.UpsertOptions;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.ReactiveQueryResult;
import com.couchbase.demo.couchmovies.service.vo.Rating;
import com.couchbase.demo.couchmovies.util.AsciiTable;
import com.couchbase.demo.couchmovies.util.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;

import static com.couchbase.client.java.kv.UpsertOptions.upsertOptions;

@Service
@Validated
public class RatingsService {

    @Autowired
    RatingsCSVParser ratingParser;
    @Autowired
    Cluster cluster;
    @Autowired
    @Qualifier("ratingsCollection")
    Collection collection;
    @Value("${com.couchbase.demo.couchmovies.my-ratings-query}")
    private String myRatingsQuery;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RatingsService() {
    }

    @Async
    public void load(long limit, boolean durability) {

        Tracer tracer = new Tracer(logger, "batchUpsert");
        UpsertOptions upsertOptions = upsertOptions();
        if (durability)
            upsertOptions.durability(DurabilityLevel.PERSIST_TO_MAJORITY);

        ratingParser.parseFromCsvFile(limit)
                .flatMap(rating -> collection.reactive().upsert(rating.getId(), rating, upsertOptions))
                .doOnNext(tracer::onNext).doOnError(tracer::onError).doOnComplete(tracer::onComplete).subscribe();
    }

    public void rate(long userId, long movieId, float rating) {

        Rating r = new Rating(userId, movieId, rating);
        r.setTimestamp(System.currentTimeMillis());

        collection.mutateIn(r.getId(), Arrays.asList(
                MutateInSpec.upsert("rating", r.getRating()),
                MutateInSpec.upsert("timestamp", r.getTimestamp())
        ));

    }

    public void findMyRatings(long userId) {

        Iterable<JsonObject> results = cluster.reactive().query(myRatingsQuery, QueryOptions.queryOptions().parameters(JsonObject.create().put("userId", userId))).flatMapMany(ReactiveQueryResult::rowsAsObject).toIterable();

        AsciiTable table = new AsciiTable();
        table.setMaxColumnWidth(50);
        table.getColumns().add(new AsciiTable.Column("movieId"));
        table.getColumns().add(new AsciiTable.Column("title"));
        table.getColumns().add(new AsciiTable.Column("rating"));
        table.getColumns().add(new AsciiTable.Column("date"));

        for (JsonObject value : results) {
            AsciiTable.Row row = new AsciiTable.Row();
            table.getData().add(row);
            row.getValues().add(value.get("movieId").toString());
            row.getValues().add(value.get("title").toString());
            row.getValues().add(value.get("rating").toString());
            row.getValues().add(value.get("date").toString());
        }

        table.calculateColumnWidth();
        table.render();
    }
}
