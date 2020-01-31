package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.opencsv.CSVReader;
import reactor.core.publisher.Flux;

import java.io.FileReader;


public interface ToJsonObjectParser {

    Flux<JsonObject> parse(int limit);

    default Flux<String[]> fromCsvFile(int limit) {
        try {

            Flux<String[]> result;

            CSVReader reader = new CSVReader(new FileReader(getPath()));

            if (limit > 0)
                result = Flux.fromIterable(reader).skip(1).limitRequest(limit);
            else
                result = Flux.fromIterable(reader).skip(1);

            return result;

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return Flux.empty();
    }

    String getPath();

}
