package com.recrieponto.ponto_eletronico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class RegistroPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usernameCoordenador;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;

    // Construtor vazio OBRIGATÓRIO para o JPA
    public RegistroPonto() {
    }

    // Nosso construtor que usamos na lógica
    public RegistroPonto(String usernameCoordenador) {
        this.usernameCoordenador = usernameCoordenador;
        this.dataHoraEntrada = LocalDateTime.now();
        this.dataHoraSaida = null;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsernameCoordenador() {
        return usernameCoordenador;
    }

    public void setUsernameCoordenador(String usernameCoordenador) {
        this.usernameCoordenador = usernameCoordenador;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
        this.dataHoraEntrada = dataHoraEntrada;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    // Métodos auxiliares que já criamos
    public String getHorasTrabalhadas() {
        if (dataHoraEntrada != null && dataHoraSaida != null) {
            Duration duracao = Duration.between(dataHoraEntrada, dataHoraSaida);
            long horas = duracao.toHours();
            long minutos = duracao.toMinutesPart();
            return String.format("%dh %02dm", horas, minutos);
        } else if (dataHoraEntrada != null) {
            return "Em andamento";
        }
        return "--";
    }

    public double getHorasTrabalhadasEmDecimal() {
        if (dataHoraEntrada != null && dataHoraSaida != null) {
            Duration duracao = Duration.between(dataHoraEntrada, dataHoraSaida);
            return duracao.toMinutes() / 60.0;
        }
        return 0;
    }
}