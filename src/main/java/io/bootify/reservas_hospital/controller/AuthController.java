package io.bootify.reservas_hospital.controller;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.bootify.reservas_hospital.domain.Usuario;
import io.bootify.reservas_hospital.domain.Paciente;
import io.bootify.reservas_hospital.repos.PacienteRepository;
import io.bootify.reservas_hospital.repos.UsuarioRepository;
import io.bootify.reservas_hospital.service.AuthService;
import io.bootify.reservas_hospital.dto.RegistroForm;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_ALLOWED = Pattern.compile("^[A-Za-z0-9._%+-]+@(gmail\\.com|outlook\\.co)$", Pattern.CASE_INSENSITIVE);

    public AuthController(AuthService authService, UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("form", new RegistroForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("form") RegistroForm form, BindingResult br, Model model) {
        // Validación de dominio permitido
        if (form.getEmail() != null && !EMAIL_ALLOWED.matcher(form.getEmail()).matches()) {
            br.rejectValue("email", "email.dominio", "El correo debe ser @gmail.com o @outlook.co");
        }

        // VALIDACIÓN DE LONGITUD (3-20 caracteres)
        if (form.getEmail() != null) {
            String localPart = form.getEmail().split("@")[0];
            if (localPart.length() < 3 || localPart.length() > 20) {
                br.rejectValue("email", "email.longitud", "El correo debe tener entre 3 y 20 caracteres antes del @");
            }
        }

        // VALIDACIÓN DE REPETICIÓN (máximo 3 veces consecutivas)
        if (form.getEmail() != null) {
            String localPart = form.getEmail().split("@")[0].toLowerCase();
            if (tieneMasDeTresRepeticiones(localPart)) {
                br.rejectValue("email", "email.repetido", "No se permiten más de 3 repeticiones consecutivas del mismo carácter");
            }
        }

        // Unicidad (normalizar a minúsculas)
        if (form.getEmail() != null && usuarioRepository.existsByEmail(form.getEmail().toLowerCase())) {
            br.rejectValue("email", "email.existe", "Este correo ya esta registrado");
        }
        // Longitud de contraseña (max 15)
        if (form.getPassword() != null && form.getPassword().length() > 15) {
            br.rejectValue("password", "password.longitud", "La contrasena no puede exceder 15 caracteres");
        }
        // Confirmación
        if (form.getPassword() != null && form.getConfirmPassword() != null && !form.getPassword().equals(form.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "password.coincide", "Las contrasenas no coinciden");
        }

        // Reglas adicionales de datos de paciente: nombres != apellidos, edad 15..95
        if (form.getNombresPaciente() != null && form.getApellidosPaciente() != null
                && form.getNombresPaciente().trim().equalsIgnoreCase(form.getApellidosPaciente().trim())) {
            br.rejectValue("apellidosPaciente", "iguales", "Nombres y apellidos no pueden ser iguales.");
        }
        if (form.getFechaNacimientoPaciente() != null) {
            java.time.LocalDate today = java.time.LocalDate.now();
            int edad = java.time.Period.between(form.getFechaNacimientoPaciente(), today).getYears();
            if (edad < 15) {
                br.rejectValue("fechaNacimientoPaciente", "edad.min", "Edad minima 15.");
            }
            if (edad > 95) {
                br.rejectValue("fechaNacimientoPaciente", "edad.max", "Edad maxima 95.");
            }
        }

        if (br.hasErrors()) {
            return "registro";
        }

        // Crear paciente y vincular al usuario
        Paciente paciente = new Paciente();
        paciente.setNombres(form.getNombresPaciente().trim());
        paciente.setApellidos(form.getApellidosPaciente().trim());
        paciente.setTelefono(form.getTelefonoPaciente().trim());
        paciente.setFechaNacimiento(form.getFechaNacimientoPaciente());
        paciente = pacienteRepository.save(paciente);

        Usuario user = new Usuario();
        // Normalizar el email a minúsculas al guardar
        user.setEmail(form.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRol("PACIENTE");
        user.setActivo(true);
        user.setPaciente(paciente);
        usuarioRepository.save(user);

        return "redirect:/login?registered=true";
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

    // comprueba si hay más de 3 repeticiones consecutivas
    private boolean tieneMasDeTresRepeticiones(String texto) {
        if (texto == null || texto.isEmpty()) return false;
        
        int contador = 1;
        for (int i = 1; i < texto.length(); i++) {
            if (texto.charAt(i) == texto.charAt(i - 1)) {
                contador++;
                if (contador > 3) {
                    return true;
                }
            } else {
                contador = 1;
            }
        }
        return false;
    }
}