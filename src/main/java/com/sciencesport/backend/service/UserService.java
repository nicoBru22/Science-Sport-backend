package com.sciencesport.backend.service;

import com.sciencesport.backend.model.User;
import com.sciencesport.backend.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux utilisateurs.
 * <p>
 * Fournit des méthodes pour :
 * <ul>
 *     <li>Rechercher un utilisateur par son nom d'utilisateur</li>
 *     <li>Ajouter un nouvel utilisateur avec hachage du mot de passe</li>
 * </ul>
 */
@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Repository pour gérer les opérations CRUD sur les utilisateurs.
     * <p>
     * Injecté automatiquement par Spring grâce à {@link org.springframework.beans.factory.annotation.Autowired}.
     * Utilisé pour rechercher, créer ou mettre à jour des instances de {@link com.sciencesport.backend.model.User}.
     * </p>
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Encodeur de mots de passe pour sécuriser les utilisateurs.
     * <p>
     * Injecté automatiquement par Spring grâce à {@link org.springframework.beans.factory.annotation.Autowired}.
     * Utilisé pour hacher les mots de passe avant de les stocker en base de données.
     * </p>
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return un {@link Optional} contenant l'utilisateur si trouvé, sinon vide
     */
    public Optional<User> findByUsername(String username) {
        logger.info("Recherche de l'utilisateur en base : {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Ajoute un nouvel utilisateur dans la base de données.
     * <p>
     * Cette méthode encode le mot de passe fourni avant de sauvegarder l'utilisateur.
     * </p>
     *
     * @param newUser l'utilisateur à créer avec mot de passe non haché
     * @return l'utilisateur créé avec mot de passe haché
     * @throws IllegalArgumentException si le mot de passe est manquant
     */
    public User addUser(User newUser) {
        String rawPassword = newUser.getMdp();

        if (rawPassword == null) {
            throw new IllegalArgumentException("Le mot de passe (mdp) est manquant !");
        }

        newUser.setMdp(passwordEncoder.encode(rawPassword));

        return userRepository.save(newUser);
    }
}