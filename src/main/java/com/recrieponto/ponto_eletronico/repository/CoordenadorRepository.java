package com.recrieponto.ponto_eletronico.repository;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {

    Optional<Coordenador> findByUsername(String username);

    List<Coordenador> findAllByRole(String role);

    // vvv NOVO MÉTODO MÁGICO vvv
    // O Spring entende que este método deve deletar um Coordenador pelo seu username
    void deleteByUsername(String username);
}