package io.bootify.reservas_hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.bootify.reservas_hospital.service.AuthService;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        if (authService.esDoctor()) {
            return "redirect:/doctor/dashboard";
        } else if (authService.esPaciente()) {
            return "redirect:/reservas/listar";
        }
        
        return "redirect:/";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}