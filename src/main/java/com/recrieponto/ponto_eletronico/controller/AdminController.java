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
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Certifique-se que este import está presente

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/dashboard")
    public String mostrarAdminDashboard(Model model, Authentication authentication) {
        model.addAttribute("nomeUsuario", authentication.getName());
        model.addAttribute("listaCoordenadores", coordenadorService.findAllCoordenadores());
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

    // --- MÉTODO CORRIGIDO ---
    @PostMapping("/coordenador/excluir/{username}")
    public String excluirCoordenador(@PathVariable String username, RedirectAttributes redirectAttributes) { // <-- 1. Adicionado RedirectAttributes
        try {
            coordenadorService.apagarPorUsername(username);
            // 2. Adiciona a mensagem de sucesso ANTES de redirecionar
            redirectAttributes.addFlashAttribute("sucesso", "Coordenador '" + username + "' excluído com sucesso!");
        } catch (Exception e) {
            // (Opcional, mas bom) Adiciona uma mensagem de erro se algo der errado
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir coordenador: " + e.getMessage());
        }
        return "redirect:/admin/dashboard"; // Volta para a dashboard do admin
    }

    @GetMapping("/registro/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<RegistroPonto> registroOpt = pontoService.findRegistroById(id);

        if (registroOpt.isPresent()) {
            RegistroPonto registro = registroOpt.get();
            model.addAttribute("registro", registro);

            String entradaFormatada = "";
            if (registro.getDataHoraEntrada() != null) {
                entradaFormatada = registro.getDataHoraEntrada().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            }
            model.addAttribute("entradaFormatada", entradaFormatada);

            String saidaFormatada = "";
            if (registro.getDataHoraSaida() != null) {
                saidaFormatada = registro.getDataHoraSaida().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            }
            model.addAttribute("saidaFormatada", saidaFormatada);

            return "editar-registro";
        } else {
            redirectAttributes.addFlashAttribute("erro", "Registro com ID " + id + " não encontrado.");
            return "redirect:/admin/dashboard";
        }
    }

    @PostMapping("/registro/editar/{id}")
    public String salvarEdicaoRegistro(@PathVariable Long id,
                                       @RequestParam("entrada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaEntrada,
                                       @RequestParam("saida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaSaida,
                                       RedirectAttributes redirectAttributes) {

        Optional<RegistroPonto> registroOpt = pontoService.findRegistroById(id);
        if (registroOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Tentativa de salvar registro inexistente.");
            return "redirect:/admin/dashboard";
        }

        String username = registroOpt.get().getUsernameCoordenador();
        try {
            pontoService.atualizarRegistroCompleto(id, novaEntrada, novaSaida);
            redirectAttributes.addFlashAttribute("sucesso", "Registro de ponto atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar registro: " + e.getMessage());
        }
        return "redirect:/admin/relatorio/" + username;
    }
}