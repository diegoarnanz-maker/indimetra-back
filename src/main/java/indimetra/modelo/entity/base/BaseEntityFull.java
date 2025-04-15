package indimetra.modelo.entity.base;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Entidad base que extiende {@link BaseEntity} e incluye campos comunes para
 * control de estado, eliminación lógica y fecha de creación.
 * <p>
 * Esta clase se debe extender en entidades que necesiten:
 * <ul>
 * <li>Activación/desactivación mediante {@code isActive}</li>
 * <li>Eliminación lógica con {@code isDeleted}</li>
 * <li>Seguimiento de fecha de creación con {@code createdAt}</li>
 * </ul>
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntityFull extends BaseEntity {

    /**
     * Indica si la entidad está activa.
     * Se establece como {@code true} por defecto.
     */
    @Column(name = "is_active", nullable = false)
    protected Boolean isActive = true;

    /**
     * Indica si la entidad ha sido eliminada lógicamente.
     * Se establece como {@code false} por defecto.
     */
    @Column(name = "is_deleted", nullable = false)
    protected Boolean isDeleted = false;

    /**
     * Fecha en la que se creó la entidad.
     * Se asigna automáticamente antes de persistir en la base de datos.
     */
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    /**
     * Establece automáticamente la fecha de creación antes de insertar la entidad.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
