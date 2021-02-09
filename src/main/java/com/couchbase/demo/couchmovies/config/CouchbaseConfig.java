package com.couchbase.demo.couchmovies.config;


import com.couchbase.client.core.env.CompressionConfig;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CouchbaseConfig {


    @Value("${com.couchbase.demo.couchmovies.connection-string}")
    private String connectionString;

    @Value("${com.couchbase.demo.couchmovies.bucket-name}")
    private String bucketName;

    @Value("${com.couchbase.demo.couchmovies.username}")
    private String username;

    @Value("${com.couchbase.demo.couchmovies.password}")
    private String password;


    @Bean
    public ClusterEnvironment couchbaseEnvironment() {
        return ClusterEnvironment.builder().compressionConfig(CompressionConfig.enable(true)).build();
    }

    @Bean
    public ReactiveCluster cluster(ClusterEnvironment env) {
        return Cluster.connect(connectionString, ClusterOptions
                .clusterOptions(username, password)
                .environment(env)).reactive();
    }

    @Bean
    public ReactiveBucket bucket(ReactiveCluster cluster) {
        return cluster.bucket(bucketName);
    }

}
