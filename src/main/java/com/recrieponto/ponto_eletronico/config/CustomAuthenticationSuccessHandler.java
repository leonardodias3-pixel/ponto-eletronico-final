package com.recrieponto.ponto_eletronico.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // --- NOSSO ESPIÃO PRINCIPAL ---
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!! SUCESSO! LOGIN FUNCIONOU, ENTRANDO NO HANDLER !!!");
        System.out.println("!!! USUÁRIO: " + authentication.getName());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("--- Verificando permissão: " + grantedAuthority.getAuthority());
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                System.out.println("--- É ADMIN! Redirecionando para /admin/dashboard...");
                response.sendRedirect("/admin/dashboard");
                return;
            }
        }

        System.out.println("--- Não é admin. Redirecionando para /ponto...");
        response.sendRedirect("/ponto");
    }
}