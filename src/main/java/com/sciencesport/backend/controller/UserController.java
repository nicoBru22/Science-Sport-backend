package com.sciencesport.backend.controller;

import com.sciencesport.backend.model.User;
import com.sciencesport.backend.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

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
}