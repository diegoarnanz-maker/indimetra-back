package indimetra.modelo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.Role.RoleType;

/**
 * Repositorio JPA para la entidad {@link Role}.
 * <p>
 * Proporciona operaciones CRUD y búsquedas personalizadas para roles de
 * usuario.
 */
public interface IRoleRepository extends JpaRepository<Role, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA POR NOMBRE
    // ============================================================

    /**
     * Busca un rol por su nombre (por ejemplo: ROLE_ADMIN, ROLE_USER).
     *
     * @param name Tipo de rol
     * @return Optional con el rol si existe
     */
    Optional<Role> findByName(RoleType name);
}
