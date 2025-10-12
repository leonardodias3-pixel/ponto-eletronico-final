package com.recrieponto.ponto_eletronico.service;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Importante

import java.util.List;
import java.util.Optional;

@Service
public class CoordenadorService {

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- NOVA DEPENDÊNCIA INJETADA AQUI ---
    @Autowired
    private PontoService pontoService;

    public boolean autenticar(String username, String senha) {
        Optional<Coordenador> coordenadorOpt = coordenadorRepository.findByUsername(username);

        if (coordenadorOpt.isPresent()) {
            Coordenador coordenador = coordenadorOpt.get();
            return passwordEncoder.matches(senha, coordenador.getPassword());
        }
        return false;
    }

    public boolean salvar(String username, String password) {
        if (coordenadorRepository.findByUsername(username).isPresent()) {
            return false;
        }
        String senhaCifrada = passwordEncoder.encode(password);
        Coordenador novoCoordenador = new Coordenador(username, senhaCifrada, "USER");
        coordenadorRepository.save(novoCoordenador);
        return true;
    }

    // Método para atualizar o admin, que usamos na classe principal
    public void salvarAdmin(Coordenador admin) {
        coordenadorRepository.save(admin);
    }

    public Optional<Coordenador> findByUsername(String username) {
        return coordenadorRepository.findByUsername(username);
    }

    public List<Coordenador> findAllCoordenadores() {
        return coordenadorRepository.findAllByRole("USER");
    }

    // --- NOVO MÉTODO ADICIONADO AQUI ---
    @Transactional
    public void apagarPorUsername(String username) {
        // 1. Primeiro, chama o PontoService para apagar os registros de ponto
        pontoService.apagarRegistrosDeUmUsuario(username);
        // 2. Depois, apaga o próprio coordenador
        coordenadorRepository.deleteByUsername(username);
    }
}