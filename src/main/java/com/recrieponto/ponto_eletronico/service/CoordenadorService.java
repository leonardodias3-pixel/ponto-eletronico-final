package com.recrieponto.ponto_eletronico.service;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordenadorService {

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autentica um usuário comparando a senha digitada (texto puro)
     * com a senha criptografada armazenada no banco de dados.
     * @return true se as senhas corresponderem, false caso contrário.
     */
    public boolean autenticar(String username, String senha) {
        Optional<Coordenador> coordenadorOpt = coordenadorRepository.findByUsername(username);

        if (coordenadorOpt.isPresent()) {
            Coordenador coordenador = coordenadorOpt.get();
            // Compara a senha crua com a senha criptografada
            return passwordEncoder.matches(senha, coordenador.getPassword());
        }
        return false;
    }

    /**
     * Salva um novo coordenador no banco de dados com a senha já criptografada.
     * @return true se o usuário foi salvo com sucesso, false se o username já existir.
     */
    public boolean salvar(String username, String password) {
        if (coordenadorRepository.findByUsername(username).isPresent()) {
            return false; // Usuário já existe
        }

        // Criptografa a senha antes de salvar
        String senhaCifrada = passwordEncoder.encode(password);

        Coordenador novoCoordenador = new Coordenador(username, senhaCifrada, "USER");
        coordenadorRepository.save(novoCoordenador);

        return true;
    }

    /**
     * Busca um Coordenador pelo seu username no banco de dados.
     * @return um Optional contendo o Coordenador se encontrado.
     */
    public Optional<Coordenador> findByUsername(String username) {
        return coordenadorRepository.findByUsername(username);
    }

    /**
     * Busca e retorna uma lista de todos os coordenadores com a role "USER".
     * Usado pela dashboard do administrador.
     * @return uma Lista de Coordenadores.
     */
    public List<Coordenador> findAllCoordenadores() {
        return coordenadorRepository.findAllByRole("USER");
    }
    // Dentro da classe CoordenadorService

    public void salvarAdmin(Coordenador admin) {
        coordenadorRepository.save(admin);
    }
}