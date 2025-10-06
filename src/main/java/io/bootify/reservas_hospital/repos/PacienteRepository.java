package io.bootify.reservas_hospital.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.bootify.reservas_hospital.domain.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByTelefono(String telefono);
}
