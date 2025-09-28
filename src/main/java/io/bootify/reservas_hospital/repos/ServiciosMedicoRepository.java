package io.bootify.reservas_hospital.repos;

import io.bootify.reservas_hospital.domain.ServiciosMedico;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServiciosMedicoRepository extends JpaRepository<ServiciosMedico, Long> {
}
