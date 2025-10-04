package io.bootify.reservas_hospital.controller;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.Paciente;
import io.bootify.reservas_hospital.domain.Reserva;
import io.bootify.reservas_hospital.domain.ServiciosMedico;
import io.bootify.reservas_hospital.domain.Usuario;
import io.bootify.reservas_hospital.repos.DoctorRepository;
import io.bootify.reservas_hospital.repos.PacienteRepository;
import io.bootify.reservas_hospital.repos.ReservaRepository;
import io.bootify.reservas_hospital.repos.ServiciosMedicoRepository;
import io.bootify.reservas_hospital.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ServiciosMedicoRepository serviciosMedicoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/nueva")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String mostrarFormularioReserva(Model model) {

        try {
            Usuario usuarioActual = authService.getUsuarioActual();
            if (usuarioActual == null) {
                return "redirect:/login?error=usuario_no_autenticado";
            }

            model.addAttribute("doctores", doctorRepository.findAll());
            model.addAttribute("servicios", serviciosMedicoRepository.findAll());
            model.addAttribute("reserva", new Reserva());
            model.addAttribute("usuarioActual", usuarioActual);

            return "nueva-reserva";

        } catch (Exception e) {
            return "redirect:/login?error=error_interno";
        }
    }

    @PostMapping("/crear")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String crearReserva(@RequestParam String nombresPaciente,
            @RequestParam String apellidosPaciente,
            @RequestParam String telefonoPaciente,
            @RequestParam String fechaNacimientoPaciente,
            @RequestParam Long doctorId,
            @RequestParam Long servicioId,
            @RequestParam String fechaReserva,
            @RequestParam String horaReserva,
            Model model) {

        try {
            // Obtener el usuario autenticado que crea la reserva
            Usuario usuarioCreador = authService.getUsuarioActual();
            if (usuarioCreador == null) {
                return "redirect:/login?error=usuario_no_autenticado";
            }

            // Crear y guardar paciente
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setNombres(nombresPaciente);
            nuevoPaciente.setApellidos(apellidosPaciente);
            nuevoPaciente.setTelefono(telefonoPaciente);
            nuevoPaciente.setFechaNacimiento(java.time.LocalDate.parse(fechaNacimientoPaciente));

            Paciente pacienteGuardado = pacienteRepository.save(nuevoPaciente);

            // Buscar doctor y servicio
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

            ServiciosMedico servicio = serviciosMedicoRepository.findById(servicioId)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

            // Crear reserva
            Reserva reserva = new Reserva();
            reserva.setPaciente(pacienteGuardado);
            reserva.setDoctor(doctor);
            reserva.setServicio(servicio);
            reserva.setFechaReserva(java.time.LocalDate.parse(fechaReserva));
            reserva.setHoraReserva(java.time.LocalTime.parse(horaReserva));
            reserva.setEstado("PENDIENTE");
            reserva.setUsuarioCreador(usuarioCreador);
            Reserva reservaGuardada = reservaRepository.save(reserva);
            return "redirect:/reservas/exito";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("doctores", doctorRepository.findAll());
            model.addAttribute("servicios", serviciosMedicoRepository.findAll());
            return "nueva-reserva";
        }
    }

    @GetMapping("/exito")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String mostrarExito() {
        return "reserva-exitosa";
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String listarReservas(Model model) {

        try {
            // Obtener el usuario autenticado
            Usuario usuarioActual = authService.getUsuarioActual();

            if (usuarioActual == null) {
                return "redirect:/login?error=usuario_no_autenticado";
            }

            // Obtener SOLO las reservas creadas por este usuario
            List<Reserva> reservas = reservaRepository.findByUsuarioCreadorId(usuarioActual.getId());

            model.addAttribute("reservas", reservas);
            model.addAttribute("usuarioActual", usuarioActual);

            return "lista-reservas";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login?error=error_interno";
        }
    }

    @PostMapping("/cancelar/{id}")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String cancelarReserva(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario autenticado
            Usuario usuarioActual = authService.getUsuarioActual();

            if (usuarioActual == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no autenticado");
                return "redirect:/login";
            }

            java.util.Optional<Reserva> reservaOpt = reservaRepository.findById(id);

            if (reservaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Reserva no encontrada");
                return "redirect:/reservas/listar";
            }
            
            Reserva reserva = reservaOpt.get();

            // Verificar que la reserva pertenezca al usuario autenticado
            if (reserva.getUsuarioCreador() == null ||
                    !reserva.getUsuarioCreador().getId().equals(usuarioActual.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para cancelar esta reserva");
                return "redirect:/reservas/listar";
            }

            // Verificar que la reserva no esté ya cancelada
            if ("CANCELADA".equals(reserva.getEstado())) {
                redirectAttributes.addFlashAttribute("warning", "La reserva ya está cancelada");
                return "redirect:/reservas/listar";
            }

            // Verificar que la reserva no esté confirmada
            if ("CONFIRMADA".equals(reserva.getEstado())) {
                redirectAttributes.addFlashAttribute("error",
                        "No se puede cancelar una reserva confirmada. Contacta con la clínica.");
                return "redirect:/reservas/listar";
            }

            // Cancelar la reserva
            reserva.setEstado("CANCELADA");
            reservaRepository.save(reserva);

            redirectAttributes.addFlashAttribute("success", "Reserva cancelada exitosamente");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la reserva: " + e.getMessage());
        }

        return "redirect:/reservas/listar";
    }
}