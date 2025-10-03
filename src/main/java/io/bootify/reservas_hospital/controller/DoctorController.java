package io.bootify.reservas_hospital.controller;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.Reserva;
import io.bootify.reservas_hospital.repos.ReservaRepository;
import io.bootify.reservas_hospital.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final ReservaRepository reservaRepository;
    private final AuthService authService;

    public DoctorController(ReservaRepository reservaRepository, AuthService authService) {
        this.reservaRepository = reservaRepository;
        this.authService = authService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public String doctorDashboard(Model model) {
        Doctor doctorActual = authService.getDoctorActual();
        
        if (doctorActual == null) {
            return "redirect:/login?error=doctor_no_encontrado";
        }

        // Obtener solo las reservas del doctor actual
        List<Reserva> reservas = reservaRepository.findByDoctorId(doctorActual.getId());
        
        model.addAttribute("reservas", reservas);
        model.addAttribute("doctor", doctorActual);
        model.addAttribute("totalReservas", reservas.size());
        
        return "doctor-dashboard";
    }

    @PostMapping("/reservas/{id}/estado")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public String actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        Doctor doctorActual = authService.getDoctorActual();
        
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        // Verificar que la reserva pertenezca al doctor actual
        if (!reserva.getDoctor().getId().equals(doctorActual.getId())) {
            throw new RuntimeException("No tienes permisos para modificar esta reserva");
        }
        
        reserva.setEstado(nuevoEstado);
        reservaRepository.save(reserva);
        return "redirect:/doctor/dashboard";
    }

    @PostMapping("/reservas/{id}/eliminar")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public String eliminarReserva(@PathVariable Long id) {
        Doctor doctorActual = authService.getDoctorActual();
        
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        // Verificar que la reserva pertenezca al doctor actual
        if (!reserva.getDoctor().getId().equals(doctorActual.getId())) {
            throw new RuntimeException("No tienes permisos para eliminar esta reserva");
        }
        
        reservaRepository.delete(reserva);
        return "redirect:/doctor/dashboard";
    }
}