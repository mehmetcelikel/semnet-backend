package com.boun.swe.semnet.sevices.config;

import com.boun.swe.semnet.commons.constants.AppConstants;
import com.boun.swe.semnet.commons.util.StringUtils;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
@EnableMongoRepositories(basePackages = "com.boun.swe.semnet.sevices.db.repo")
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${app.profile}")
    private String profile;

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private int port;

    @Value("${mongodb.name}")
    private String db;

    @Value("${mongodb.replicaSet}")
    private String replicaSet;

    @Override
    public String getDatabaseName() {
        return db;
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        Mongo mongo;
        if(profile.equals(AppConstants.PROD)) {
            List<ServerAddress> serverAddresses = new LinkedList<ServerAddress>();
            for(String replica : StringUtils.splitByComma(replicaSet)) {
                serverAddresses.add(new ServerAddress(replica, port));
            }
            mongo = new MongoClient(serverAddresses);
            mongo.setReadPreference(ReadPreference.secondaryPreferred());

        } else {
            mongo = new MongoClient(singletonList(new ServerAddress(host, port)));
        }
        mongo.setWriteConcern(WriteConcern.SAFE);

        return mongo;
    }

}
