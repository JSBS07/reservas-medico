package io.bootify.reservas_hospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol; // DOCTOR o PACIENTE

    @Column(nullable = false)
    private Boolean activo = true;

    // Relación opcional con Doctor si es doctor
    @OneToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Relación opcional con Paciente si es paciente
    @OneToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @Column(nullable = false)
    private OffsetDateTime ultimaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = OffsetDateTime.now();
        this.ultimaActualizacion = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.ultimaActualizacion = OffsetDateTime.now();
    }
}