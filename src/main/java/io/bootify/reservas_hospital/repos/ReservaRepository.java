package io.bootify.reservas_hospital.repos;

import io.bootify.reservas_hospital.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
