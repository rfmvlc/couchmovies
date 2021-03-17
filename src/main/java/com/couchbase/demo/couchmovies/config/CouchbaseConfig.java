package com.couchbase.demo.couchmovies.config;


import com.couchbase.client.core.env.CompressionConfig;
import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.core.env.ThresholdRequestTracerConfig;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.env.ClusterEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

import java.time.Duration;


@Configuration
@EnableReactiveCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {


    @Value("${com.couchbase.demo.couchmovies.connection-string}")
    private String connectionString;

    @Value("${com.couchbase.demo.couchmovies.bucket-name}")
    private String bucketName;

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
        Bucket bucket = cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(5));
        return bucket;
    }

    @Bean
    public Collection collection(Bucket bucket) {
        return bucket.defaultCollection();
    }

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    public String typeKey() {
        return "type";
    }
}
