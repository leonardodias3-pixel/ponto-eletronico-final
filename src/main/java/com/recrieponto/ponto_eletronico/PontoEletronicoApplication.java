package com.recrieponto.ponto_eletronico;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.repository.CoordenadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PontoEletronicoApplication {

    public static void main(String[] args) {
        // --- INÍCIO DO NOSSO ESPIÃO ---
        // Vamos imprimir os valores das variáveis de ambiente que a aplicação está "enxergando".
        System.out.println("!!!!!!!!!!!!!!!!!! INICIANDO TESTE DE AMBIENTE !!!!!!!!!!!!!!!!!!");
        System.out.println("!!! LENDO VARIAVEL SPRING_DATASOURCE_URL: " + System.getenv("SPRING_DATASOURCE_URL"));
        System.out.println("!!! LENDO VARIAVEL SPRING_DATASOURCE_USERNAME: " + System.getenv("SPRING_DATASOURCE_USERNAME"));
        System.out.println("!!! LENDO VARIAVEL SPRING_DATASOURCE_PASSWORD: " + System.getenv("SPRING_DATASOURCE_PASSWORD"));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // --- FIM DO ESPIÃO ---

        SpringApplication.run(PontoEletronicoApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CoordenadorRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByUsername("admingeral").isEmpty()) {
                System.out.println(">>> Criando usuário admin padrão...");
                String senhaCriptografada = passwordEncoder.encode("admin123");
                Coordenador admin = new Coordenador("admingeral", senhaCriptografada, "ADMIN");
                repository.save(admin);
                System.out.println(">>> Usuário admin criado.");
            }
        };
    }
}