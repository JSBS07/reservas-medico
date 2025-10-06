package io.bootify.reservas_hospital.controller;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.Paciente;
import io.bootify.reservas_hospital.domain.Reserva;
import io.bootify.reservas_hospital.domain.ServiciosMedico;
import io.bootify.reservas_hospital.domain.Usuario;
import io.bootify.reservas_hospital.dto.ReservaPacienteForm;
import io.bootify.reservas_hospital.repos.DoctorRepository;
import io.bootify.reservas_hospital.repos.PacienteRepository;
import io.bootify.reservas_hospital.repos.ReservaRepository;
import io.bootify.reservas_hospital.repos.ServiciosMedicoRepository;
import io.bootify.reservas_hospital.service.AuthService;
import io.bootify.reservas_hospital.service.BusinessCalendarService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private BusinessCalendarService businessCalendarService;

    @GetMapping("/nueva")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String mostrarFormularioReserva(Model model) {
        try {
            Usuario usuarioActual = authService.getUsuarioActual();
            if (usuarioActual == null) {
                return "redirect:/login?error=usuario_no_autenticado";
            }
            model.addAttribute("form", new ReservaPacienteForm());
            model.addAttribute("doctores", doctorRepository.findAll());
            model.addAttribute("servicios", serviciosMedicoRepository.findAll());
            model.addAttribute("usuarioActual", usuarioActual);
            return "nueva-reserva";
        } catch (Exception e) {
            return "redirect:/login?error=error_interno";
        }
    }

    @PostMapping("/crear")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String crearReserva(
            @Valid @ModelAttribute("form") ReservaPacienteForm form,
            BindingResult br,
            Model model) {

        if (br.hasErrors()) {
            model.addAttribute("doctores", doctorRepository.findAll());
            model.addAttribute("servicios", serviciosMedicoRepository.findAll());
            return "nueva-reserva";
        }

        Usuario usuario = authService.getUsuarioActual();
        if (usuario == null) {
            return "redirect:/login?error=usuario_no_autenticado";
        }

        LocalDate fechaReserva = form.getFechaReserva();
        if (fechaReserva != null) {
            LocalDate today = LocalDate.now();
            if (fechaReserva.isBefore(today)) {
                br.rejectValue("fechaReserva", "fecha.pasada", "La fecha debe ser hoy o futura.");
            }
            if (!br.hasFieldErrors("fechaReserva") && businessCalendarService.isWeekend(fechaReserva)) {
                br.rejectValue("fechaReserva", "fecha.finDeSemana", "No se pueden agendar citas los fines de semana.");
            }
            if (!br.hasFieldErrors("fechaReserva") && businessCalendarService.isHoliday(fechaReserva)) {
                br.rejectValue("fechaReserva", "fecha.festivo", "No se pueden agendar citas en dias festivos.");
            }
        }

        LocalTime horaReserva = form.getHoraReserva();
        if (horaReserva != null && (horaReserva.isBefore(LocalTime.of(7, 0)) || horaReserva.isAfter(LocalTime.of(17, 0)))) {
            br.rejectValue("horaReserva", "hora.fueraRango", "La hora debe estar entre 7:00 y 17:00.");
        }

        if (form.getNombresPaciente() != null && form.getApellidosPaciente() != null
                && form.getNombresPaciente().trim().equalsIgnoreCase(form.getApellidosPaciente().trim())) {
            br.rejectValue("apellidosPaciente", "iguales", "Nombres y apellidos no pueden ser iguales.");
        }

        if (form.getFechaNacimientoPaciente() != null) {
            int edad = Period.between(form.getFechaNacimientoPaciente(), LocalDate.now()).getYears();
            if (edad < 15) {
                br.rejectValue("fechaNacimientoPaciente", "edad.min", "Edad minima 15.");
            }
            if (edad > 95) {
                br.rejectValue("fechaNacimientoPaciente", "edad.max", "Edad maxima 95.");
            }
        }

        if (br.hasErrors()) {
            model.addAttribute("doctores", doctorRepository.findAll());
            model.addAttribute("servicios", serviciosMedicoRepository.findAll());
            return "nueva-reserva";
        }

        Doctor doctor = doctorRepository.findById(form.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        ServiciosMedico servicio = serviciosMedicoRepository.findById(form.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        LocalDate fecha = form.getFechaReserva();
        LocalTime hora = form.getHoraReserva();
        int duracionNueva = normalizeDuration(servicio.getDuracionMinutos());

        if (fecha != null && hora != null) {
            List<Reserva> reservasDoctor = reservaRepository
                    .findByDoctorIdAndFechaReservaAndEstadoNot(
                            doctor.getId(), fecha, "CANCELADA");
            boolean doctorOcupado = reservasDoctor.stream()
                    .filter(reservaExistente -> reservaExistente.getHoraReserva() != null)
                    .anyMatch(reservaExistente -> overlaps(
                            reservaExistente.getHoraReserva(),
                            normalizeDuration(reservaExistente.getServicio() != null
                                    ? reservaExistente.getServicio().getDuracionMinutos()
                                    : null),
                            hora,
                            duracionNueva));
            if (doctorOcupado) {
                br.reject("conflicto.doctor", "El doctor ya tiene una reserva en ese horario.");
            }
        }

        Paciente paciente = pacienteRepository.findByTelefono(form.getTelefonoPaciente()).orElse(null);
        if (paciente == null) {
            paciente = new Paciente();
            paciente.setNombres(form.getNombresPaciente().trim());
            paciente.setApellidos(form.getApellidosPaciente().trim());
            paciente.setTelefono(form.getTelefonoPaciente().trim());
            paciente.setFechaNacimiento(form.getFechaNacimientoPaciente());
            paciente = pacienteRepository.save(paciente);
        }

        if (fecha != null && hora != null) {
            List<Reserva> reservasPaciente = reservaRepository
                    .findByPacienteIdAndFechaReservaAndEstadoNot(
                            paciente.getId(), fecha, "CANCELADA");
            boolean pacienteDuplicado = reservasPaciente.stream()
                    .filter(reservaExistente -> reservaExistente.getHoraReserva() != null)
                    .anyMatch(reservaExistente -> overlaps(
                            reservaExistente.getHoraReserva(),
                            normalizeDuration(reservaExistente.getServicio() != null
                                    ? reservaExistente.getServicio().getDuracionMinutos()
                                    : null),
                            hora,
                            duracionNueva));
            if (pacienteDuplicado) {
                br.reject("conflicto.paciente", "Ya tienes una reserva en ese horario.");
            }
        }

        if (br.hasErrors()) {
            model.addAttribute("doctores", doctorRepository.findAll());
            model.addAttribute("servicios", serviciosMedicoRepository.findAll());
            return "nueva-reserva";
        }

        Reserva reserva = new Reserva();
        reserva.setPaciente(paciente);
        reserva.setDoctor(doctor);
        reserva.setServicio(servicio);
        reserva.setFechaReserva(fecha);
        reserva.setHoraReserva(hora);
        reserva.setEstado("PENDIENTE");
        reserva.setUsuarioCreador(usuario);

        reservaRepository.save(reserva);

        return "redirect:/reservas/exito";
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String listarReservas(Model model) {
        Usuario usuario = authService.getUsuarioActual();
        if (usuario == null) {
            return "redirect:/login?error=usuario_no_autenticado";
        }
        model.addAttribute("reservas", reservaRepository.findByUsuarioCreadorId(usuario.getId()));
        model.addAttribute("usuarioActual", usuario);
        return "lista-reservas";
    }

    @GetMapping("/exito")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String mostrarExito() {
        return "reserva-exitosa";
    }

    @PostMapping("/cancelar/{id}")
    @PreAuthorize("hasAuthority('PACIENTE')")
    public String cancelarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = authService.getUsuarioActual();
        if (usuario == null) {
            return "redirect:/login?error=usuario_no_autenticado";
        }

        Reserva reserva = reservaRepository.findById(id).orElse(null);
        if (reserva == null || !reserva.getUsuarioCreador().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute("error", "Reserva no encontrada o no te pertenece.");
            return "redirect:/reservas/listar";
        }

        if (!"PENDIENTE".equalsIgnoreCase(reserva.getEstado())) {
            redirectAttributes.addFlashAttribute("warning", "Solo puedes cancelar reservas pendientes.");
            return "redirect:/reservas/listar";
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
        redirectAttributes.addFlashAttribute("success", "La reserva fue cancelada correctamente.");
        return "redirect:/reservas/listar";
    }

    private static boolean overlaps(LocalTime existingStart, int existingDurationMinutes,
            LocalTime newStart, int newDurationMinutes) {
        LocalTime existingEnd = existingStart.plusMinutes(existingDurationMinutes);
        LocalTime newEnd = newStart.plusMinutes(newDurationMinutes);
        return existingStart.isBefore(newEnd) && newStart.isBefore(existingEnd);
    }

    private static int normalizeDuration(Integer durationMinutes) {
        return (durationMinutes != null && durationMinutes > 0) ? durationMinutes : 30;
    }
}


