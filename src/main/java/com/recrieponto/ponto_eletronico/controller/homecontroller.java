package com.recrieponto.ponto_eletronico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homecontroller {

    @GetMapping("/")
    public String home() {
        // Thymeleaf vai buscar o arquivo /resources/templates/index.html
        return "index";
    }
}