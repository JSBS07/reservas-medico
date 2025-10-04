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
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("🔐 AuthService.getUsuarioActual() - Authentication: " + authentication);
            
            if (authentication == null) {
                System.out.println("❌ Authentication es null");
                return null;
            }
            
            if (!authentication.isAuthenticated()) {
                System.out.println("❌ Authentication no está autenticado");
                return null;
            }
            
            String email = authentication.getName();
            
            // Buscar el usuario en la base de datos
            java.util.Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            
            if (usuarioOpt.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado: " + email);
            }
            
            Usuario usuario = usuarioOpt.get();
            System.out.println("✅ Usuario encontrado - ID: " + usuario.getId() + 
                             ", Email: " + usuario.getEmail() + 
                             ", Rol: " + usuario.getRol() +
                             ", Activo: " + usuario.getActivo());
            
            return usuario;
            
        } catch (Exception e) {
            System.out.println("💥 ERROR en getUsuarioActual: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener usuario actual: " + e.getMessage());
        }
    }

    public Doctor getDoctorActual() {
        Usuario usuario = getUsuarioActual();
        if (usuario != null && "DOCTOR".equals(usuario.getRol())) {
            return usuario.getDoctor();
        }
        return null;
    }

    public boolean esDoctor() {
        Usuario usuario = getUsuarioActual();
        boolean esDoctor = usuario != null && "DOCTOR".equals(usuario.getRol());
        System.out.println("👨‍⚕️ Es doctor: " + esDoctor);
        return esDoctor;
    }

    public boolean esPaciente() {
        Usuario usuario = getUsuarioActual();
        boolean esPaciente = usuario != null && "PACIENTE".equals(usuario.getRol());
        System.out.println("👤 Es paciente: " + esPaciente);
        return esPaciente;
    }
}