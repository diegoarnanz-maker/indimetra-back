package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;

import indimetra.modelo.entity.base.BaseEntityCreated;

/**
 * Entidad que representa un rol asignado a los usuarios del sistema.
 * Los roles determinan los permisos y accesos disponibles.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntityCreated {

    /**
     * Nombre del rol. Debe ser único.
     * Puede ser por ejemplo: ROLE_ADMIN, ROLE_USER.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    /** Descripción del rol */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Enumeración de los tipos de rol disponibles en la aplicación.
     */
    public enum RoleType {
        ROLE_ADMIN, ROLE_USER
    }
}
