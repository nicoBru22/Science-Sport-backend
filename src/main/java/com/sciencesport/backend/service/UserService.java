package com.sciencesport.backend.service;

import com.sciencesport.backend.model.User;
import com.sciencesport.backend.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        logger.info("Recherche de l'utilisateur en base : {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Méthode dédiée à l'inscription (Register)
     */
    public User addUser(User newUser) {
        // On récupère le mot de passe brut envoyé via le champ 'mdp'
        String rawPassword = newUser.getMdp();

        if (rawPassword == null) {
            throw new IllegalArgumentException("Le mot de passe (mdp) est manquant !");
        }

        // On hache et on réinjecte dans 'mdp'
        newUser.setMdp(passwordEncoder.encode(rawPassword));

        return userRepository.save(newUser);
    }
}