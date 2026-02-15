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
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;

    @Size(min = 8)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    // On n'utilise que 'mdp' pour éviter les conflits avec Spring Security
    // WRITE_ONLY = Jackson lit le mdp quand Angular l'envoie,
    // mais il ne l'écrit jamais quand Java répond à Angular.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String mdp;

    private Set<String> roles;

    // --- Implémentation de UserDetails ---

    @Override
    public String getPassword() {
        return this.mdp;
    }

    // On crée un setter explicite pour que UserService puisse modifier le mdp
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return Collections.emptyList();
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}