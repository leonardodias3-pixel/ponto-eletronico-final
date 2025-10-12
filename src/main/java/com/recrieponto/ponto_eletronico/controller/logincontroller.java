package com.recrieponto.ponto_eletronico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class logincontroller {

    // A única função deste controller agora é MOSTRAR a página de login.
    // O processamento do login e do logout é 100% feito pelo Spring Security.
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
}