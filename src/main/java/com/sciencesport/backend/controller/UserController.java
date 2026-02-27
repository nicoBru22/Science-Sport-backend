package com.sciencesport.backend.controller;

import com.sciencesport.backend.model.User;
import com.sciencesport.backend.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        logger.info("Tentative d'inscription pour l'utilisateur : {}", user.getUsername());

        try {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                logger.warn("Échec inscription : l'utilisateur {} existe déjà", user.getUsername());
                return ResponseEntity
                        .status(HttpStatus.CONFLICT) // 409 est plus précis que 400 ici
                        .body(Map.of("message", "Erreur : Ce nom d'utilisateur est déjà pris."));
            }

            User createdUser = userService.addUser(user);
            logger.info("Utilisateur créé avec succès : ID = {}", createdUser.getId());

            // On renvoie l'utilisateur sans son mot de passe pour la sécurité
            createdUser.setMdp(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (Exception e) {
            logger.error("Erreur critique lors de l'inscription de {} : {}", user.getUsername(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur serveur : " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        logger.info("Tentative de connexion pour login : {}", username);

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            credentials.get("password")
                    )
            );

            logger.info("Connexion réussie pour l'utilisateur : {}", auth.getName());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "username", auth.getName()
            ));

        } catch (AuthenticationException e) {
            logger.error("Échec de connexion pour {} : {}", username, e.getMessage(), e);
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Identifiants incorrects"
            ));
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        logger.info("Vérification d'authentification pour : {}", authentication.getName());

        // On cherche les infos complètes en base si besoin
        return userService.findByUsername(authentication.getName())
                .map(user -> {
                    user.setMdp(null); // Sécurité : ne jamais renvoyer le hash
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Ici on peut invalider la session si elle existe
        SecurityContextHolder.clearContext(); // Nettoie l'authentification
        return ResponseEntity.ok(Map.of("status", "success", "message", "Déconnexion réussie"));
    }
}