package com.sciencesport.backend.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDbConfig {

    @Bean
    public MongoClient mongoClient() {
        // Ta chaîne de connexion Atlas directement ici
        return MongoClients.create("mongodb+srv://brunetnicolas35_db_user:Rt2kv7q-1@cluster0.wgczbl9.mongodb.net/SportScience?retryWrites=true&w=majority");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        // On lie le client à la base de données SportScience
        return new MongoTemplate(mongoClient(), "SportScience");
    }
}
