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

/**
 * Contrôleur REST pour gérer l'authentification et les opérations sur les utilisateurs.
 * <p>
 * Fournit des endpoints pour :
 * <ul>
 *     <li>Inscription d'un nouvel utilisateur</li>
 *     <li>Connexion d'un utilisateur</li>
 *     <li>Récupération des informations de l'utilisateur connecté</li>
 *     <li>Déconnexion</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Inscrit un nouvel utilisateur.
     *
     * @param user l'utilisateur à créer
     * @return {@link ResponseEntity} contenant l'utilisateur créé (sans mot de passe) ou un message d'erreur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        logger.info("Tentative d'inscription pour l'utilisateur : {}", user.getUsername());

        try {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                logger.warn("Échec inscription : l'utilisateur {} existe déjà", user.getUsername());
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Erreur : Ce nom d'utilisateur est déjà pris."));
            }

            User createdUser = userService.addUser(user);
            logger.info("Utilisateur créé avec succès : ID = {}", createdUser.getId());

            createdUser.setMdp(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (Exception e) {
            logger.error("Erreur critique lors de l'inscription de {} : {}", user.getUsername(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur serveur : " + e.getMessage()));
        }
    }

    /**
     * Authentifie un utilisateur avec ses identifiants.
     *
     * @param credentials Map contenant "username" et "password"
     * @return {@link ResponseEntity} contenant le statut et le nom d'utilisateur si succès,
     *         sinon un message d'erreur 401 si échec d'authentification
     */
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Identifiants incorrects"
            ));
        }
    }

    /**
     * Récupère les informations de l'utilisateur actuellement connecté.
     *
     * @param authentication l'objet {@link Authentication} fourni par Spring Security
     * @return {@link ResponseEntity} contenant l'utilisateur (sans mot de passe),
     *         401 si non authentifié, ou 404 si utilisateur non trouvé
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        logger.info("Vérification d'authentification pour : {}", authentication.getName());

        return userService.findByUsername(authentication.getName())
                .map(user -> {
                    user.setMdp(null);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Déconnecte l'utilisateur courant en nettoyant le contexte de sécurité.
     *
     * @return {@link ResponseEntity} avec message de succès
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("status", "success", "message", "Déconnexion réussie"));
    }
}