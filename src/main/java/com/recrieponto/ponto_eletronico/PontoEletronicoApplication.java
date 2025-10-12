package com.recrieponto.ponto_eletronico;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.repository.CoordenadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORT

@SpringBootApplication
public class PontoEletronicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PontoEletronicoApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CoordenadorRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByUsername("admingeral").isEmpty()) {
                System.out.println(">>> Criando usuário admin padrão (COM SENHA CRIPTOGRAFADA)...");

                // Criptografa a senha aqui mesmo
                String senhaCriptografada = passwordEncoder.encode("admin123");

                // Cria o objeto com a senha já criptografada
                Coordenador admin = new Coordenador("admingeral", senhaCriptografada, "ADMIN");

                // Salva diretamente no banco
                repository.save(admin);

                System.out.println(">>> Usuário admin criado.");
            }
        };
    }
}