package com.couchbase.demo.couchmovies.config;


import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
public class CouchbaseConfig {


    @Value("${com.couchbase.demo.couchmovies.connection-string}")
    private String connectionString;

    @Value("${com.couchbase.demo.couchmovies.username}")
    private String username;

    @Value("${com.couchbase.demo.couchmovies.password}")
    private String password;

    @Bean
    public Cluster couchbaseCluster() {
        PasswordAuthenticator authenticator = PasswordAuthenticator
                .builder()
                .username(username)
                .password(password)
                .onlyEnablePlainSaslMechanism()
                .build();

        Cluster cluster = Cluster.connect(connectionString, ClusterOptions.clusterOptions(authenticator));
        cluster.waitUntilReady(Duration.ofSeconds(5));
        return cluster;

    }

    @Bean
    public Bucket bucket(Cluster cluster) {
        Bucket bucket = cluster.bucket(getBucketName());
        bucket.waitUntilReady(Duration.ofSeconds(5));
        return bucket;
    }

    @Bean
    public Collection moviesCollection(Bucket bucket) {
        return bucket.scope("couchmovies").collection("movies");
    }

    @Bean
    public Collection ratingsCollection(Bucket bucket) {
        return bucket.scope("couchmovies").collection("ratings");
    }

    public String getBucketName() {
        return "movies";
    }


}
