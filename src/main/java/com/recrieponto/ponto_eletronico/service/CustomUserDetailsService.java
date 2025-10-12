package com.recrieponto.ponto_eletronico.service;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usa nosso repository para buscar o Coordenador no banco de dados
        Coordenador coordenador = coordenadorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        // Retorna o objeto Coordenador, que agora o Spring Security entende porque implementa UserDetails
        return coordenador;
    }
}