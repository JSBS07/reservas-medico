package io.bootify.reservas_hospital.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ServiciosMedico {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "servicio_sequence",
            sequenceName = "servicio_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "servicio_sequence"
    )
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombreServicio;

    @Column(nullable = false)
    private Integer duracionMinutos;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column
    private Boolean activo = true;

    @OneToMany(mappedBy = "servicio")
    private Set<Reserva> servicioReservas = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;
}