package com.couchbase.demo.couchmovies.util;

import reactor.core.publisher.Flux;

import java.io.FileReader;


public interface CSVReader {

    default Flux<String[]> fromCsvFile(long limit) {
        try {

            Flux<String[]> result;

            com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader(getPath()));

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
