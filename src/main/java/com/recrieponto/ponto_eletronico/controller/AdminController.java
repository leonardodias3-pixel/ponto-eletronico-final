package com.recrieponto.ponto_eletronico.controller;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.service.CoordenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // <-- IMPORT NOVO
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CoordenadorService coordenadorService;

    // A anotação @GetMapping já é protegida pelo Spring Security.
    // Não precisamos mais verificar a role aqui dentro.
    @GetMapping("/dashboard")
    public String mostrarAdminDashboard(Model model, Authentication authentication) { // <-- REMOVEMOS HttpSession

        // MODO CORRETO DE PEGAR O NOME DO USUÁRIO LOGADO COM SPRING SECURITY
        String username = authentication.getName();

        List<Coordenador> listaCoordenadores = coordenadorService.findAllCoordenadores();

        model.addAttribute("nomeUsuario", username);
        model.addAttribute("listaCoordenadores", listaCoordenadores);

        return "admin-dashboard";
    }

    // O método de ver o relatório individual continua igual, mas vamos limpá-lo também.
    // (Vou omitir por enquanto para focarmos na correção principal, mas ele também deve ser limpo)
}