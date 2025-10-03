package io.bootify.reservas_hospital.repos;

import io.bootify.reservas_hospital.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
}
