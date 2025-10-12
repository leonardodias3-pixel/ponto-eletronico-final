package com.recrieponto.ponto_eletronico.repository;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // <-- ADICIONE ESTE IMPORT
import java.util.Optional;

public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {

    Optional<Coordenador> findByUsername(String username);

    // vvv ADICIONE ESTE NOVO MÉTODO ABAIXO vvv

    // Método mágico que busca todos os Coordenadores que têm uma 'role' específica.
    List<Coordenador> findAllByRole(String role);
}