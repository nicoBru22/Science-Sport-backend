package com.sciencesport.backend.service;

import com.sciencesport.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implémentant {@link UserDetailsService} pour Spring Security.
 * <p>
 * Ce service permet de charger un utilisateur depuis la base de données MongoDB
 * en utilisant {@link UserRepository} et de le transformer en {@link UserDetails}
 * compréhensible par Spring Security.
 * </p>
 *
 * Utilisé principalement pour l'authentification.
 *
 * @see UserDetailsService
 */
@Service
public class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Charge un utilisateur par son nom d'utilisateur pour Spring Security.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return {@link UserDetails} représentant l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé pour ce nom
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.sciencesport.backend.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // Conversion du modèle User en UserDetails pour Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER") // Obligatoire : Spring Security requiert au moins un rôle
                .build();
    }
}