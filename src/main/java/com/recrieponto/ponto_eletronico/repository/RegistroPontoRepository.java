package com.recrieponto.ponto_eletronico.repository;

import com.recrieponto.ponto_eletronico.model.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    // ... (métodos existentes continuam iguais)
    List<RegistroPonto> findAllByUsernameCoordenadorOrderByDataHoraEntradaDesc(String username);
    Optional<RegistroPonto> findFirstByUsernameCoordenadorOrderByDataHoraEntradaDesc(String username);
    List<RegistroPonto> findAllByUsernameCoordenadorAndDataHoraEntradaAfter(String username, LocalDateTime data);
    List<RegistroPonto> findAllByOrderByDataHoraEntradaDesc();

    // vvv NOVO MÉTODO MÁGICO vvv
    // Deleta TODOS os registros de ponto que pertencem a um usuário específico
    void deleteAllByUsernameCoordenador(String username);
}