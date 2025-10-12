package com.recrieponto.ponto_eletronico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class Coordenador implements UserDetails { // <-- 1. IMPLEMENTA UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;

    public Coordenador() {}

    public Coordenador(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- MÉTODOS DE UserDetails ADICIONADOS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 2. Transforma nossa "role" (ex: "ADMIN") em algo que o Spring Security entende.
        // O "ROLE_" é um prefixo padrão do Spring Security.
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // Para simplificar, vamos retornar 'true' para os métodos de status da conta.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }


    // --- GETTERS E SETTERS NORMAIS (CONTINUAM IGUAIS) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}