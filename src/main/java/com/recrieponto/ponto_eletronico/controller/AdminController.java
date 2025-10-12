package com.recrieponto.ponto_eletronico.controller;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.service.CoordenadorService;
import com.recrieponto.ponto_eletronico.service.PontoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Import corrigido
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PontoService pontoService;

    @Autowired
    private CoordenadorService coordenadorService;

    // Método para a dashboard principal do admin (lista os coordenadores)
    @GetMapping("/dashboard")
    public String mostrarAdminDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Coordenador> listaCoordenadores = coordenadorService.findAllCoordenadores();

        model.addAttribute("nomeUsuario", username);
        model.addAttribute("listaCoordenadores", listaCoordenadores);

        return "admin-dashboard";
    }

    // O MÉTODO QUE ESTAVA FALTANDO!
    // Ele responde ao endereço /admin/relatorio/{qualquer-nome-de-usuario}
    @GetMapping("/relatorio/{username}")
    public String verRelatorioDoCoordenador(@PathVariable String username, Model model, Authentication authentication) {
        // A segurança já é garantida pela SecurityConfig, não precisamos verificar a role aqui.

        // Reutiliza a mesma lógica do PontoController, mas para o usuário selecionado
        Map<String, Double> dadosGrafico = pontoService.getHorasTrabalhadasPorDiaDaSemana(username);
        boolean isGraficoVazio = dadosGrafico.values().stream().mapToDouble(Double::doubleValue).sum() == 0;

        model.addAttribute("nomeUsuario", username);
        model.addAttribute("statusPonto", pontoService.verificarStatus(username));
        model.addAttribute("registros", pontoService.getRegistrosDoUsuario(username));
        model.addAttribute("dadosGraficoSemanal", dadosGrafico);
        model.addAttribute("isGraficoVazio", isGraficoVazio);

        return "relatorio-coordenador"; // Retorna a nova página de relatório
    }
    @PostMapping("/coordenador/excluir/{username}")
    public String excluirCoordenador(@PathVariable String username) {
        coordenadorService.apagarPorUsername(username);
        return "redirect:/admin/dashboard"; // Volta para a dashboard do admin
    }
}