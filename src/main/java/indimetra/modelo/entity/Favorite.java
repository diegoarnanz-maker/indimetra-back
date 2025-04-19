package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;

import indimetra.modelo.entity.base.BaseEntityFull;

/**
 * Entidad que representa un cortometraje marcado como favorito por un usuario.
 * Cada combinación de usuario y cortometraje debe ser única.
 */
@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "cortometraje_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite extends BaseEntityFull {

    /** Usuario que ha marcado el cortometraje como favorito */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Cortometraje marcado como favorito */
    @ManyToOne
    @JoinColumn(name = "cortometraje_id", nullable = false)
    private Cortometraje cortometraje;
}
