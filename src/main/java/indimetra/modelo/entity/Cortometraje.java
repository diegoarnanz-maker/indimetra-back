package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import indimetra.modelo.entity.base.BaseEntityFull;

/**
 * Entidad que representa un cortometraje dentro del sistema.
 */
@Entity
@Table(name = "cortometrajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cortometraje extends BaseEntityFull {

    /** Título único del cortometraje */
    @Column(unique = true, nullable = false)
    private String title;

    /** Descripción general del cortometraje */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Técnica utilizada en la realización del cortometraje (animación, stop motion,
     * etc.)
     */
    @Column(length = 50)
    private String technique;

    /** Año de lanzamiento del cortometraje */
    @Column(name = "release_year", nullable = false)
    private int releaseYear;

    /** Duración del cortometraje en minutos */
    @Column(nullable = false)
    private int duration;

    /** Idioma principal del cortometraje */
    @Column(nullable = false, length = 50)
    private String language;

    /** Valoración media del cortometraje (calculada a partir de reviews) */
    @Column(precision = 3, scale = 1)
    private BigDecimal rating;

    /** URL de la imagen asociada al cortometraje */
    @Column(name = "image_url")
    private String imageUrl;

    /** URL del video (puede ser un link embebido o directo) */
    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    /** Usuario creador del cortometraje */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Categoría a la que pertenece el cortometraje */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Método ejecutado automáticamente antes de guardar el registro.
     * Asigna el idioma por defecto si no se ha definido.
     */
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (this.language == null) {
            this.language = "Spanish";
        }
    }
}
