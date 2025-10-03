package io.bootify.reservas_hospital.service;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.Usuario;
import io.bootify.reservas_hospital.repos.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
    }

    public Doctor getDoctorActual() {
        Usuario usuario = getUsuarioActual();
        if (usuario != null && usuario.getRol().equals("DOCTOR")) {
            return usuario.getDoctor();
        }
        return null;
    }

    public boolean esDoctor() {
        Usuario usuario = getUsuarioActual();
        return usuario != null && usuario.getRol().equals("DOCTOR");
    }

    public boolean esPaciente() {
        Usuario usuario = getUsuarioActual();
        return usuario != null && usuario.getRol().equals("PACIENTE");
    }
}