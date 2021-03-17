package com.couchbase.demo.couchmovies.service;

import com.opencsv.CSVReader;
import reactor.core.publisher.Flux;

import java.io.FileReader;


public interface FluxCsvReader {

    default Flux<String[]> fromCsvFile(long limit) {
        try {

            Flux<String[]> result;

            CSVReader reader = new CSVReader(new FileReader(getPath()));

            if (limit > 0)
                result = Flux.fromIterable(reader).limitRequest(limit);
            else
                result = Flux.fromIterable(reader);

            return result;

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return Flux.empty();
    }

    String getPath();

}
