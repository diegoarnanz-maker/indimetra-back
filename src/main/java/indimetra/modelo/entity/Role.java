package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;

import indimetra.modelo.entity.base.BaseEntity;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Enum de roles
    public enum RoleType {
        ROLE_ADMIN, ROLE_USER
    }
}
