package io.bootify.reservas_hospital.controller;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.Paciente;
import io.bootify.reservas_hospital.domain.Reserva;
import io.bootify.reservas_hospital.domain.ServiciosMedico;
import io.bootify.reservas_hospital.repos.DoctorRepository;
import io.bootify.reservas_hospital.repos.PacienteRepository;
import io.bootify.reservas_hospital.repos.ReservaRepository;
import io.bootify.reservas_hospital.repos.ServiciosMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/nueva")
    public String mostrarFormularioReserva(Model model) {
        System.out.println("Accediendo a /reservas/nueva");
        model.addAttribute("doctores", doctorRepository.findAll());
        model.addAttribute("servicios", serviciosMedicoRepository.findAll());
        model.addAttribute("reserva", new Reserva());
        return "nueva-reserva";
    }

    @PostMapping("/crear")
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
            System.out.println("Creando reserva para: " + nombresPaciente + " " + apellidosPaciente);

            // Crear y guardar paciente nuevo 
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

            reservaRepository.save(reserva);

            System.out.println("Reserva creada exitosamente para: " + nombresPaciente + " " + apellidosPaciente);
            return "redirect:/reservas/exito";

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("doctores", doctorRepository.findAll());
            model.addAttribute("servicios", serviciosMedicoRepository.findAll());
            return "nueva-reserva";
        }
    }

    @GetMapping("/exito")
    public String mostrarExito() {
        return "reserva-exitosa";
    }

    @GetMapping("/listar")
    public String listarReservas(Model model) {
        System.out.println("✅ Accediendo a /reservas/listar");

        List<Reserva> reservas = reservaRepository.findAll();
        System.out.println("📊 Reservas encontradas: " + reservas.size());

        for (Reserva reserva : reservas) {
            String pacienteNombre = "";
            try {
                pacienteNombre = reserva.getPaciente().getNombreCompleto();
            } catch (Exception ex) {
                pacienteNombre = reserva.getPaciente().getNombres() + " " + reserva.getPaciente().getApellidos();
            }
            System.out.println("   - Reserva ID: " + reserva.getId() +
                    ", Paciente: " + pacienteNombre +
                    ", Estado: " + reserva.getEstado());
        }

        model.addAttribute("reservas", reservas);
        return "lista-reservas";
    }
}
