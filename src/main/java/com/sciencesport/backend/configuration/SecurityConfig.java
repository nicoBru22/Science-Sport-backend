package com.sciencesport.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration de sécurité pour l'application Spring Boot.
 * <p>
 * Cette classe configure :
 * <ul>
 *     <li>Le filtre de sécurité HTTP {@link SecurityFilterChain}</li>
 *     <li>L'encodage des mots de passe avec {@link PasswordEncoder}</li>
 *     <li>Les règles CORS pour l'application</li>
 *     <li>Le {@link AuthenticationManager} pour l'authentification Spring Security</li>
 * </ul>
 *
 * Fonctionnalités principales :
 * <ul>
 *     <li>Désactivation de CSRF</li>
 *     <li>Gestion de session avec {@link SessionCreationPolicy#IF_REQUIRED}</li>
 *     <li>Ouverture des endpoints publics pour l'authentification et les articles</li>
 *     <li>Protection de tous les autres endpoints</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     *
     * @param http l'objet {@link HttpSecurity} fourni par Spring Security
     * @return la {@link SecurityFilterChain} configurée
     * @throws Exception si une erreur survient lors de la configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/articles/**").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/me").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    /**
     * Bean pour encoder les mots de passe avec BCrypt.
     *
     * @return un {@link PasswordEncoder} utilisant BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration CORS pour permettre les requêtes depuis l'application frontend.
     *
     * @return la source de configuration CORS {@link CorsConfigurationSource}
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Bean pour gérer l'authentification avec Spring Security.
     *
     * @param userDetailsService le service fournissant les détails des utilisateurs
     * @param passwordEncoder l'encodeur de mot de passe
     * @return un {@link AuthenticationManager} configuré
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authentication -> authProvider.authenticate(authentication);
    }
}