package com.recrieponto.ponto_eletronico.controller;

import com.recrieponto.ponto_eletronico.model.RegistroPonto;
import com.recrieponto.ponto_eletronico.service.PontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // <-- IMPORT NOVO
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ponto")
public class PontoController {

    @Autowired
    private PontoService pontoService;

    // Este método só será chamado se o usuário tiver a ROLE_USER, graças à SecurityConfig.
    @GetMapping
    public String home(Model model, Authentication authentication) { // <-- REMOVEMOS HttpSession
        String username = authentication.getName();

        String statusPonto = pontoService.verificarStatus(username);
        List<RegistroPonto> registrosDoUsuario = pontoService.getRegistrosDoUsuario(username);
        Map<String, Double> dadosGrafico = pontoService.getHorasTrabalhadasPorDiaDaSemana(username);
        boolean isGraficoVazio = dadosGrafico.values().stream().mapToDouble(Double::doubleValue).sum() == 0;

        model.addAttribute("nomeUsuario", username);
        model.addAttribute("statusPonto", statusPonto);
        model.addAttribute("registros", registrosDoUsuario);
        model.addAttribute("dadosGraficoSemanal", dadosGrafico);
        model.addAttribute("isGraficoVazio", isGraficoVazio);

        return "dashboard";
    }

    @PostMapping("/registrar")
    public String registrarPonto(Authentication authentication) { // <-- REMOVEMOS HttpSession
        String username = authentication.getName();
        pontoService.registrarPonto(username);
        return "redirect:/ponto";
    }
}