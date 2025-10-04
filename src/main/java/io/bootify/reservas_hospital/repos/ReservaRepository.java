package io.bootify.reservas_hospital.repos;

import io.bootify.reservas_hospital.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    List<Reserva> findByDoctorId(Long doctorId);
    List<Reserva> findByPacienteId(Long pacienteId);
    List<Reserva> findByUsuarioCreadorId(Long usuarioCreadorId);
    Optional<Reserva> findById(Long id);
}