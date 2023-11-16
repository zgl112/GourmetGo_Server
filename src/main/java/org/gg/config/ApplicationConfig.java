package org.gg.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfig {
    @Value("${db.connectionString}")
    private String mongodbHost;

    @Bean
    public MongoClient mongoClient(){

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongodbHost))
                .build();

        return MongoClients.create(settings);
    }
}
