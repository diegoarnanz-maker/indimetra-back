package indimetra.modelo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import indimetra.modelo.entity.base.BaseEntityFull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa a un usuario dentro del sistema.
 * Implementa {@link UserDetails} para integración con Spring Security.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntityFull implements UserDetails {

    /** Nombre de usuario único utilizado para autenticación */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /** Correo electrónico único del usuario */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** Contraseña cifrada del usuario */
    @Column(nullable = false)
    private String password;

    /** URL de la imagen de perfil del usuario */
    @Column(name = "profile_image")
    private String profileImage;

    /** Indica si el usuario tiene el perfil de autor */
    @Builder.Default
    @Column(name = "is_author")
    private Boolean isAuthor = false;

    /** Enlaces a redes sociales u otras referencias del usuario (opcional) */
    @Column(name = "social_links")
    private String socialLinks;

    /** País de residencia del usuario */
    @Column(nullable = false, length = 50)
    private String country;

    /**
     * Roles asignados al usuario (relación muchos a muchos).
     * Se cargan con eager loading para disponibilidad inmediata.
     */
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Devuelve las autoridades (roles) del usuario para Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                .toList();
    }

    /**
     * Indica si la cuenta está habilitada (activa y no eliminada).
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.isActive) && Boolean.FALSE.equals(this.isDeleted);
    }
}
