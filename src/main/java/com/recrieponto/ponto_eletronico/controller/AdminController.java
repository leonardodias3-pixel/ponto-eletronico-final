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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // <-- IMPORT NOVO
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

    // ... (os outros métodos como dashboard, relatório e excluir continuam iguais)
    @GetMapping("/dashboard")
    public String mostrarAdminDashboard(Model model, Authentication authentication) { /* ...código existente... */ }

    @GetMapping("/relatorio/{username}")
    public String verRelatorioDoCoordenador(@PathVariable String username, Model model) { /* ...código existente... */ }

    @PostMapping("/coordenador/excluir/{username}")
    public String excluirCoordenador(@PathVariable String username) { /* ...código existente... */ }


    // --- MÉTODO CORRIGIDO ---
    @GetMapping("/registro/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<RegistroPonto> registroOpt = pontoService.findRegistroById(id);

        if (registroOpt.isPresent()) {
            RegistroPonto registro = registroOpt.get();
            model.addAttribute("registro", registro);

            // LÓGICA MOVIDA PARA CÁ: Pré-formatamos a data de saída
            String saidaFormatada = "";
            if (registro.getDataHoraSaida() != null) {
                // Formato exigido pelo input datetime-local
                saidaFormatada = registro.getDataHoraSaida().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            }
            model.addAttribute("saidaFormatada", saidaFormatada);

            return "editar-registro";
        } else {
            redirectAttributes.addFlashAttribute("erro", "Registro com ID " + id + " não encontrado.");
            return "redirect:/admin/dashboard";
        }
    }

    // O método de salvar continua igual
    @PostMapping("/registro/editar/{id}")
    public String salvarEdicaoRegistro(@PathVariable Long id,
                                       @RequestParam("saida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaSaida,
                                       RedirectAttributes redirectAttributes) { /* ...código existente... */ }
}