package io.bootify.reservas_hospital.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeResource {

    @GetMapping("/")
    public String index() {
        return "redirect:/reservas/nueva";
    }
}