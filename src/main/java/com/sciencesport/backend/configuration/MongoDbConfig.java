package com.sciencesport.backend.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configuration pour la connexion à MongoDB.
 * <p>
 * Cette classe configure :
 * <ul>
 *     <li>Le {@link MongoClient} pour se connecter à la base MongoDB</li>
 *     <li>Le {@link MongoTemplate} pour effectuer des opérations sur la base de données "SportScience"</li>
 * </ul>
 *
 * Les informations de connexion sont injectées depuis la variable d'environnement {@code MONGODB_URI}.
 */
@Configuration
public class MongoDbConfig {

    /**
     * URI de connexion MongoDB, injectée depuis application.properties ou .env.
     */
    @Value("${MONGODB_URI}")
    private String mongoUri;

    /**
     * Crée et configure le {@link MongoClient}.
     *
     * @return un {@link MongoClient} connecté à MongoDB
     */
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    /**
     * Crée et configure le {@link MongoTemplate} pour effectuer des opérations sur la base "SportScience".
     *
     * @return un {@link MongoTemplate} opérationnel
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "SportScience");
    }
}