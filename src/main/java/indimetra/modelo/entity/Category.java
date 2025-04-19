package indimetra.modelo.entity;

import indimetra.modelo.entity.base.BaseEntityFull;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una categoría de cortometrajes.
 * <p>
 * Cada categoría tiene un nombre único y una descripción opcional.
 * Hereda campos comunes como {@code id}, {@code isActive}, {@code isDeleted} y
 * {@code createdAt}
 * desde {@link BaseEntityFull}.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntityFull {

    /**
     * Nombre de la categoría.
     * Debe ser único y no nulo. Máximo 50 caracteres.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Descripción detallada de la categoría.
     * Este campo se almacena como texto largo.
     */
    @Column(columnDefinition = "TEXT")
    private String description;
}
