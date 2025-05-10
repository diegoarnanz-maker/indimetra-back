package indimetra.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Role.RoleType;
import indimetra.modelo.entity.User;

/**
 * Repositorio JPA para la entidad {@link User}.
 * <p>
 * Proporciona operaciones CRUD y consultas específicas para usuarios.
 */
public interface IUserRepository extends JpaRepository<User, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA POR CAMPOS ÚNICOS
    // ============================================================

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por su email.
     *
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findByEmail(String email);

    // ============================================================
    // 🔍 BÚSQUEDA CON FILTROS DE ESTADO (activos y no eliminados)
    // ============================================================

    /**
     * Busca un usuario activo y no eliminado por su nombre de usuario.
     */
    Optional<User> findByUsernameAndIsActiveTrueAndIsDeletedFalse(String username);

    /**
     * Busca un usuario activo y no eliminado por su email.
     */
    Optional<User> findByEmailAndIsActiveTrueAndIsDeletedFalse(String email);

    /**
     * Busca usuarios cuyo nombre de usuario contenga el texto indicado (ignorando
     * mayúsculas/minúsculas),
     * y que estén activos y no eliminados.
     */
    List<User> findByUsernameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String username);

    /**
     * Busca usuarios por tipo de rol, siempre que estén activos y no eliminados.
     */
    List<User> findByRoles_NameAndIsActiveTrueAndIsDeletedFalse(RoleType name);

    /**
     * Obtiene una página de usuarios activos y no eliminados.
     */
    Page<User> findByIsActiveTrueAndIsDeletedFalse(Pageable pageable);

    /**
     * Obtiene una página de usuarios activos y no eliminados por rol.
     */
    Page<User> findByIsActiveFalseAndIsDeletedFalse(Pageable pageable);

    /**
     * Obtiene una página de usuarios activos y no eliminados por rol.
     */
    Page<User> findByIsDeletedTrue(Pageable pageable);

    /**
     * Obtiene todos los usuarios activos y no eliminados.
     */
    List<User> findByIsActiveTrueAndIsDeletedFalse();
}
