package com.sciencesport.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Représente un utilisateur stocké en base de données MongoDB.
 * Implémente {@link UserDetails} pour l'intégration avec Spring Security.
 *
 * @author [Ton nom ou Sciencesport]
 * @version 1.0
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /**
     * Identifiant unique de l'utilisateur, généré automatiquement par MongoDB.
     */
    @Id
    private String id;

    /**
     * Nom d'utilisateur, utilisé pour l'authentification.
     * Doit contenir au moins 8 caractères et ne comporter que des lettres et chiffres.
     */
    @Size(min = 8)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    /**
     * Mot de passe de l'utilisateur, toujours masqué dans les réponses JSON.
     * Utilisé uniquement en entrée pour des raisons de sécurité.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String mdp;

    /**
     * Rôles de l'utilisateur, utilisés pour gérer les autorisations.
     */
    private Set<String> roles;

    // --- Implémentation de UserDetails ---

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return this.mdp;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     * @param mdp le nouveau mot de passe
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    /**
     * {@inheritDoc}
     * Convertit les rôles en {@link GrantedAuthority} pour Spring Security.
     * @return une collection d'autorisations
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return Collections.emptyList();
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * @return toujours true, car les comptes n'expirent pas dans cette implémentation
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return toujours true, car les comptes ne sont jamais verrouillés
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return toujours true, car les identifiants n'expirent pas
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return toujours true, car les comptes sont toujours activés
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
