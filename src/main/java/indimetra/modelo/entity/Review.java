package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import indimetra.modelo.entity.base.BaseEntityFull;

/**
 * Entidad que representa una reseña realizada por un usuario sobre un
 * cortometraje.
 * Incluye la puntuación y un comentario opcional.
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntityFull {

    /** Usuario que realizó la reseña */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Cortometraje al que pertenece la reseña */
    @ManyToOne
    @JoinColumn(name = "cortometraje_id", nullable = false)
    private Cortometraje cortometraje;

    /**
     * Puntuación otorgada al cortometraje (escala de 0-5)
     */
    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal rating;

    /** Comentario del usuario sobre el cortometraje */
    @Column(columnDefinition = "TEXT")
    private String comment;
}
