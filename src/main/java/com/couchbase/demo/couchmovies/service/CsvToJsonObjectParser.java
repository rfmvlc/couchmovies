package com.couchbase.demo.couchmovies.service;

import com.couchbase.client.java.json.JsonObject;
import com.opencsv.CSVReader;
import reactor.core.publisher.Flux;

import java.io.FileReader;


public interface CsvToJsonObjectParser {

    Flux<JsonObject> parseFromCsvFile(long limit, long skip, boolean random);

    default Flux<String[]> fromCsvFile(long limit, long skip) {
        try {

            Flux<String[]> result;

            CSVReader reader = new CSVReader(new FileReader(getPath()));

            if (limit > 0)
                result = Flux.fromIterable(reader).skip(1 + skip).limitRequest(limit);
            else
                result = Flux.fromIterable(reader).skip(1 + skip);

            return result;

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return Flux.empty();
    }

    String getPath();

}
