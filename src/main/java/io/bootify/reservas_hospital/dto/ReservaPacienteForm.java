package io.bootify.reservas_hospital.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import io.bootify.reservas_hospital.validation.ValidName;
import io.bootify.reservas_hospital.validation.ColombiaPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class ReservaPacienteForm {

    // Nombres y apellidos: solo letras y un espacio simple
    @NotBlank(message = "Los nombres son obligatorios.")
    @Size(min = 2, max = 60, message = "Los nombres deben tener entre 2 y 60 caracteres.")
    @ValidName(message = "Ingrese nombres validos: solo letras, hasta 4 palabras, sin repetir y cada palabra con vocal.")
    private String nombresPaciente;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Size(min = 2, max = 60, message = "Los apellidos deben tener entre 2 y 60 caracteres.")
    @ValidName(message = "Ingrese apellidos validos: solo letras, hasta 4 palabras, sin repetir y cada palabra con vocal.")
    private String apellidosPaciente;

    // Telefono Colombia: se valida con ColombiaPhone para soportar prefijo +57 y formatos comunes
    @NotBlank(message = "El telefono es obligatorio.")
    @ColombiaPhone(message = "Ingrese un telefono colombiano valido. Puede incluir prefijo +57 y separadores comunes.")
    private String telefonoPaciente;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser pasada.")
    private LocalDate fechaNacimientoPaciente;

    @NotNull(message = "Debe seleccionar un doctor.")
    private Long doctorId;

    @NotNull(message = "Debe seleccionar un servicio.")
    private Long servicioId;

    @NotNull(message = "La fecha de la reserva es obligatoria.")
    private LocalDate fechaReserva;

    @NotNull(message = "La hora de la reserva es obligatoria.")
    private LocalTime horaReserva;

    // Getters & Setters

    public String getNombresPaciente() { return nombresPaciente; }
    public void setNombresPaciente(String nombresPaciente) { this.nombresPaciente = nombresPaciente; }

    public String getApellidosPaciente() { return apellidosPaciente; }
    public void setApellidosPaciente(String apellidosPaciente) { this.apellidosPaciente = apellidosPaciente; }

    public String getTelefonoPaciente() { return telefonoPaciente; }
    public void setTelefonoPaciente(String telefonoPaciente) { this.telefonoPaciente = telefonoPaciente; }

    public LocalDate getFechaNacimientoPaciente() { return fechaNacimientoPaciente; }
    public void setFechaNacimientoPaciente(LocalDate fechaNacimientoPaciente) { this.fechaNacimientoPaciente = fechaNacimientoPaciente; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public LocalTime getHoraReserva() { return horaReserva; }
    public void setHoraReserva(LocalTime horaReserva) { this.horaReserva = horaReserva; }
}
