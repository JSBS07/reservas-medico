package io.bootify.reservas_hospital.controller;

import io.bootify.reservas_hospital.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
            return "redirect:/reservas/nueva";
        }
        
        return "redirect:/";
    }

    @GetMapping("/")
    public String home() {
        if (authService.getUsuarioActual() != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
}