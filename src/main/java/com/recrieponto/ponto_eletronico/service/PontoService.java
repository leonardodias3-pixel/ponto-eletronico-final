package com.recrieponto.ponto_eletronico.service;

import com.recrieponto.ponto_eletronico.model.RegistroPonto;
import com.recrieponto.ponto_eletronico.repository.RegistroPontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId; // <-- Import adicionado
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class PontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    public void registrarPonto(String username) {
        Optional<RegistroPonto> ultimoRegistroOpt = registroPontoRepository.findFirstByUsernameCoordenadorOrderByDataHoraEntradaDesc(username);

        if (ultimoRegistroOpt.isEmpty() || ultimoRegistroOpt.get().getDataHoraSaida() != null) {
            RegistroPonto novoRegistro = new RegistroPonto(username);
            registroPontoRepository.save(novoRegistro);
        } else {
            RegistroPonto registroAberto = ultimoRegistroOpt.get();
            // MUDANÇA CRÍTICA: Captura a hora de saída especificamente do fuso de São Paulo
            registroAberto.setDataHoraSaida(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
            registroPontoRepository.save(registroAberto);
        }
    }

    public String verificarStatus(String username) {
        Optional<RegistroPonto> ultimoRegistroOpt = registroPontoRepository.findFirstByUsernameCoordenadorOrderByDataHoraEntradaDesc(username);

        if (ultimoRegistroOpt.isEmpty() || ultimoRegistroOpt.get().getDataHoraSaida() != null) {
            return "Seu ponto está FECHADO.";
        } else {
            RegistroPonto registroAberto = ultimoRegistroOpt.get();
            if (registroAberto.getDataHoraEntrada() != null) {
                return "Ponto ABERTO desde: " + registroAberto.getDataHoraEntrada().toLocalTime().withNano(0).toString();
            }
            return "Ponto ABERTO.";
        }
    }

    public List<RegistroPonto> getRegistrosDoUsuario(String username) {
        return registroPontoRepository.findAllByUsernameCoordenadorOrderByDataHoraEntradaDesc(username);
    }

    public Map<String, Double> getHorasTrabalhadasPorDiaDaSemana(String username) {
        Map<String, Double> dadosGrafico = new LinkedHashMap<>();
        LocalDate hoje = LocalDate.now();
        LocalDateTime seteDiasAtras = hoje.minusDays(6).atStartOfDay();

        List<RegistroPonto> registrosDaSemana = registroPontoRepository.findAllByUsernameCoordenadorAndDataHoraEntradaAfter(username, seteDiasAtras);

        for (int i = 6; i >= 0; i--) {
            LocalDate dia = hoje.minusDays(i);
            String nomeDia = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));

            double horasNoDia = registrosDaSemana.stream()
                    .filter(r -> r.getDataHoraEntrada() != null && r.getDataHoraEntrada().toLocalDate().equals(dia))
                    .mapToDouble(RegistroPonto::getHorasTrabalhadasEmDecimal)
                    .sum();

            dadosGrafico.put(nomeDia.toUpperCase(), horasNoDia);
        }
        return dadosGrafico;
    }
}