package com.recrieponto.ponto_eletronico.controller;

import com.recrieponto.ponto_eletronico.model.Coordenador;
import com.recrieponto.ponto_eletronico.model.RegistroPonto;
import com.recrieponto.ponto_eletronico.service.CoordenadorService;
import com.recrieponto.ponto_eletronico.service.PontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional; // <-- Import adicionado

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PontoService pontoService;

    @Autowired
    private CoordenadorService coordenadorService;

    @GetMapping("/dashboard")
    public String mostrarAdminDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Coordenador> listaCoordenadores = coordenadorService.findAllCoordenadores();

        model.addAttribute("nomeUsuario", username);
        model.addAttribute("listaCoordenadores", listaCoordenadores);

        return "admin-dashboard";
    }

    @GetMapping("/relatorio/{username}")
    public String verRelatorioDoCoordenador(@PathVariable String username, Model model) {
        Map<String, Double> dadosGrafico = pontoService.getHorasTrabalhadasPorDiaDaSemana(username);
        boolean isGraficoVazio = dadosGrafico.values().stream().mapToDouble(Double::doubleValue).sum() == 0;

        model.addAttribute("nomeUsuario", username);
        model.addAttribute("statusPonto", pontoService.verificarStatus(username));
        model.addAttribute("registros", pontoService.getRegistrosDoUsuario(username));
        model.addAttribute("dadosGraficoSemanal", dadosGrafico);
        model.addAttribute("isGraficoVazio", isGraficoVazio);

        return "relatorio-coordenador";
    }

    @PostMapping("/coordenador/excluir/{username}")
    public String excluirCoordenador(@PathVariable String username) {
        coordenadorService.apagarPorUsername(username);
        return "redirect:/admin/dashboard";
    }

    // --- MÉTODO CORRIGIDO ---
    @GetMapping("/registro/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        // Primeiro, buscamos o registro e guardamos em um Optional
        Optional<RegistroPonto> registroOpt = pontoService.findRegistroById(id);

        // Verificamos se o Optional contém um valor
        if (registroOpt.isPresent()) {
            // Se sim, adicionamos ao model e mostramos a página de edição
            model.addAttribute("registro", registroOpt.get());
            return "editar-registro";
        } else {
            // Se não, o ID era inválido. Redirecionamos de volta para a segurança da dashboard.
            return "redirect:/admin/dashboard";
        }
    }

    @PostMapping("/registro/editar/{id}")
    public String salvarEdicaoRegistro(@PathVariable Long id,
                                       @RequestParam("saida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaSaida) {

        String username = pontoService.findRegistroById(id)
                .map(RegistroPonto::getUsernameCoordenador)
                .orElseThrow(() -> new IllegalArgumentException("Registro não encontrado"));

        pontoService.atualizarRegistroSaida(id, novaSaida);

        return "redirect:/admin/relatorio/" + username;
    }
}