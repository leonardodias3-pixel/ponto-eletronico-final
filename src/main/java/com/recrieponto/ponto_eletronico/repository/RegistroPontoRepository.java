package com.recrieponto.ponto_eletronico.repository;

import com.recrieponto.ponto_eletronico.model.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    /**
     * Magic Method: Encontra TODOS os registros de um usuário específico,
     * ordenados pela data de entrada, do mais recente para o mais antigo.
     */
    List<RegistroPonto> findAllByUsernameCoordenadorOrderByDataHoraEntradaDesc(String username);

    /**
     * Magic Method: Encontra APENAS O ÚLTIMO (o mais recente) registro
     * de um usuário específico.
     */
    Optional<RegistroPonto> findFirstByUsernameCoordenadorOrderByDataHoraEntradaDesc(String username);
    List<RegistroPonto> findAllByUsernameCoordenadorAndDataHoraEntradaAfter(String username, LocalDateTime data);
    List<RegistroPonto> findAllByOrderByDataHoraEntradaDesc();
}