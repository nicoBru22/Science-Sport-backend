package com.sciencesport.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuration globale CORS pour l'application.
 * <p>
 * Cette classe fournit un {@link CorsFilter} pour autoriser les requêtes cross-origin
 * depuis des origines spécifiques (frontend Angular ou GitHub Pages par exemple).
 * </p>
 *
 * Fonctionnalités principales :
 * <ul>
 *     <li>Autorise les requêtes avec cookies et authentification</li>
 *     <li>Définit les origines autorisées</li>
 *     <li>Définit les headers et méthodes HTTP autorisés</li>
 * </ul>
 */
@Configuration
public class CorsConfig {

    /**
     * Crée le {@link CorsFilter} pour gérer les requêtes CORS entrantes.
     *
     * @return un {@link CorsFilter} configuré avec les origines, headers et méthodes autorisées
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "https://nicobru22.github.io"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}