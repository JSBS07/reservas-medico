package io.bootify.reservas_hospital.repos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.bootify.reservas_hospital.domain.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByDoctorId(Long doctorId);

    List<Reserva> findByPacienteId(Long pacienteId);

    List<Reserva> findByUsuarioCreadorId(Long usuarioCreadorId);

    List<Reserva> findByDoctorIdAndFechaReservaAndEstadoNot(
            Long doctorId, LocalDate fecha, String estadoExcluido);

    List<Reserva> findByPacienteIdAndFechaReservaAndEstadoNot(
            Long pacienteId, LocalDate fecha, String estadoExcluido);

    boolean existsByDoctorIdAndFechaReservaAndHoraReservaAndEstadoNot(
            Long doctorId, LocalDate fecha, LocalTime hora, String estadoExcluido);

    boolean existsByPacienteIdAndFechaReservaAndHoraReservaAndEstadoNot(
            Long pacienteId, LocalDate fecha, LocalTime hora, String estadoExcluido);
}
