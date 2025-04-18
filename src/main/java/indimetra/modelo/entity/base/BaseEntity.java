package indimetra.modelo.entity.base;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Clase base abstracta para todas las entidades del sistema.
 * <p>
 * Define un campo `id` común, marcado como clave primaria y auto-generado.
 * Ideal para heredar en entidades JPA y evitar redundancia.
 * </p>
 *
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity implements Serializable {

    /**
     * Identificador único de la entidad.
     * <p>
     * Se genera automáticamente mediante una estrategia de identidad (auto-incremental).
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}
