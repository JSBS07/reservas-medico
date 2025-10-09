package io.bootify.reservas_hospital.dto;

import java.time.LocalDate;

import io.bootify.reservas_hospital.validation.ValidName;
import io.bootify.reservas_hospital.validation.ColombiaPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class RegistroForm {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingrese un correo valido")
    @Size(max = 100, message = "Maximo 100 caracteres")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(max = 15, message = "Maximo 15 caracteres")
    private String password;

    @NotBlank(message = "Confirme la contrasena")
    @Size(max = 15, message = "Maximo 15 caracteres")
    private String confirmPassword;

    // Datos del paciente al registrarse
    @NotBlank(message = "Los nombres son obligatorios.")
    @Size(min = 2, max = 60, message = "Los nombres deben tener entre 2 y 60 caracteres.")
    @ValidName(message = "Ingrese nombres validos: solo letras, hasta 4 palabras, sin repetir y cada palabra con vocal.")
    private String nombresPaciente;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Size(min = 2, max = 60, message = "Los apellidos deben tener entre 2 y 60 caracteres.")
    @ValidName(allowRepeatedWords = true, message = "Ingrese apellidos validos: solo letras, hasta 4 palabras y cada palabra con vocal (se permiten apellidos iguales).")
    private String apellidosPaciente;

    @NotBlank(message = "El telefono es obligatorio.")
    @ColombiaPhone(message = "Ingrese un telefono colombiano valido. Puede incluir prefijo +57 y separadores comunes.")
    private String telefonoPaciente;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser pasada.")
    private LocalDate fechaNacimientoPaciente;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getNombresPaciente() { return nombresPaciente; }
    public void setNombresPaciente(String nombresPaciente) { this.nombresPaciente = nombresPaciente; }

    public String getApellidosPaciente() { return apellidosPaciente; }
    public void setApellidosPaciente(String apellidosPaciente) { this.apellidosPaciente = apellidosPaciente; }

    public String getTelefonoPaciente() { return telefonoPaciente; }
    public void setTelefonoPaciente(String telefonoPaciente) { this.telefonoPaciente = telefonoPaciente; }

    public LocalDate getFechaNacimientoPaciente() { return fechaNacimientoPaciente; }
    public void setFechaNacimientoPaciente(LocalDate fechaNacimientoPaciente) { this.fechaNacimientoPaciente = fechaNacimientoPaciente; }
}
