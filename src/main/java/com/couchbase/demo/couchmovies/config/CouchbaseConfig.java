package com.couchbase.demo.couchmovies.config;


import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;


@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {


    @Value("${com.couchbase.demo.couchmovies.connection-string}")
    private String connectionString;

    @Value("${com.couchbase.demo.couchmovies.bucket-name}")
    private String bucketName;

    @Value("${com.couchbase.demo.couchmovies.username}")
    private String username;

    @Value("${com.couchbase.demo.couchmovies.password}")
    private String password;


    //@Bean
    //public ClusterEnvironment couchbaseEnvironment() {
    //    return ClusterEnvironment.builder().compressionConfig(CompressionConfig.enable(true)).build();
    //}

    @Bean
    //public ReactiveCluster cluster(ClusterEnvironment env) {
    public Cluster cluster() {

        PasswordAuthenticator authenticator = PasswordAuthenticator
                .builder()
                .username(username)
                .password(password)
                .onlyEnablePlainSaslMechanism()
                .build();

        return Cluster.connect(connectionString, ClusterOptions.clusterOptions(authenticator));
        //.environment(env)
    }

    @Bean
    public Bucket bucket(Cluster cluster) {
        return cluster().bucket(bucketName);
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
