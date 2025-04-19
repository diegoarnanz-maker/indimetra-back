package indimetra.modelo.entity.base;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Entidad base que extiende {@link BaseEntity} para incluir la fecha de
 * creación.
 * <p>
 * Proporciona un campo {@code createdAt} que se asigna automáticamente antes de
 * persistir
 * la entidad en la base de datos.
 * </p>
 * 
 * <p>
 * <b>Uso:</b> Heredar esta clase cuando se necesite registrar la fecha de
 * creación de una entidad.
 * </p>
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntityCreated extends BaseEntity {

    /**
     * Fecha en la que se creó la entidad.
     * Se establece automáticamente antes de la persistencia.
     */
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    /**
     * Asigna la fecha actual al campo {@code createdAt} antes de insertar la
     * entidad en la base de datos.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
