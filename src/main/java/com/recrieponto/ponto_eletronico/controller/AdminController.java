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
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PontoService pontoService;

    @Autowired
    private CoordenadorService coordenadorService;

    // Método da dashboard principal (já estava correto)
    @GetMapping("/dashboard")
    public String mostrarAdminDashboard(Model model, Authentication authentication) {
        model.addAttribute("nomeUsuario", authentication.getName());
        model.addAttribute("listaCoordenadores", coordenadorService.findAllCoordenadores());
        return "admin-dashboard";
    }

    // Método do relatório individual (padronizado para não usar HttpSession)
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

    // Método para excluir (já estava correto)
    @PostMapping("/coordenador/excluir/{username}")
    public String excluirCoordenador(@PathVariable String username) {
        coordenadorService.apagarPorUsername(username);
        return "redirect:/admin/dashboard";
    }

    // Método para mostrar o formulário de edição (com a lógica robusta)
    @GetMapping("/registro/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<RegistroPonto> registroOpt = pontoService.findRegistroById(id);

        if (registroOpt.isPresent()) {
            model.addAttribute("registro", registroOpt.get());
            return "editar-registro";
        } else {
            // Se não encontrar o registro, redireciona para a segurança da dashboard
            return "redirect:/admin/dashboard?erro=registro_nao_encontrado";
        }
    }

    // Método para salvar a edição (já estava correto)
    @PostMapping("/registro/editar/{id}")
    public String salvarEdicaoRegistro(@PathVariable Long id,
                                       @RequestParam("saida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaSaida) {

        String username = pontoService.findRegistroById(id)
                .map(RegistroPonto::getUsernameCoordenador)
                .orElseThrow(() -> new IllegalArgumentException("Registro não encontrado para redirecionamento"));

        pontoService.atualizarRegistroSaida(id, novaSaida);

        return "redirect:/admin/relatorio/" + username;
    }
}