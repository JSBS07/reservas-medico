package io.bootify.reservas_hospital.repos;

import io.bootify.reservas_hospital.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
