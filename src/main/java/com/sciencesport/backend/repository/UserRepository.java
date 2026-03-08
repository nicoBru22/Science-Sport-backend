package com.sciencesport.backend.repository;

import com.sciencesport.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface pour la gestion des utilisateurs dans la base de données MongoDB.
 * <p>
 * Cette interface étend {@link MongoRepository} pour fournir des opérations CRUD (Create, Read, Update, Delete)
 * sur les objets {@link User}. Elle inclut également des méthodes de recherche personnalisées.
 * </p>
 *
 * @see User
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Recherche un utilisateur par son nom d'utilisateur (username).
     *
     * @param username le nom d'utilisateur à rechercher
     * @return un {@link Optional} contenant l'utilisateur si trouvé, sinon vide
     */
    Optional<User> findByUsername(String username);
}