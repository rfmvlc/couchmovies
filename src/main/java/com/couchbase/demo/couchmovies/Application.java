package com.couchbase.demo.couchmovies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.setProperty("com.couchbase.autoReleaseAfter", "10000");
        SpringApplication.run(Application.class, args);

    }

}
