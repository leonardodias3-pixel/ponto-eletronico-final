package com.recrieponto.ponto_eletronico.controller;

import com.recrieponto.ponto_eletronico.service.CoordenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CadastroController {

    @Autowired
    private CoordenadorService coordenadorService;

    // Método para MOSTRAR a página de cadastro
    @GetMapping("/cadastro")
    public String mostrarPaginaDeCadastro() {
        return "cadastro";
    }

    // Método para PROCESSAR o formulário de cadastro
    @PostMapping("/cadastro")
    public String processarCadastro(@RequestParam String username,
                                    @RequestParam String password,
                                    Model model) {

        boolean sucesso = coordenadorService.salvar(username, password);

        if (sucesso) {
            // Se o cadastro deu certo, redireciona para a página de login
            return "redirect:/login?cadastro=sucesso";
        } else {
            // Se o usuário já existe, volta para a página de cadastro com uma mensagem de erro
            model.addAttribute("erro", "Nome de usuário já existe. Tente outro.");
            return "cadastro";
        }
    }
}