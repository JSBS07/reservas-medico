package io.bootify.reservas_hospital.service;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.Usuario;
import io.bootify.reservas_hospital.repos.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            LOG.debug("No authentication available in the security context");
            return null;
        }

        if (!authentication.isAuthenticated()) {
            LOG.debug("Authentication {} is not marked as authenticated", authentication.getClass().getSimpleName());
            return null;
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            LOG.debug("Anonymous authentication detected");
            return null;
        }

        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
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
        LOG.debug("esDoctor: {}", esDoctor);
        return esDoctor;
    }

    public boolean esPaciente() {
        Usuario usuario = getUsuarioActual();
        boolean esPaciente = usuario != null && "PACIENTE".equals(usuario.getRol());
        LOG.debug("esPaciente: {}", esPaciente);
        return esPaciente;
    }
}
