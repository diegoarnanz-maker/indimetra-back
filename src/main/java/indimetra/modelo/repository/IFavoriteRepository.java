package indimetra.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Favorite;

/**
 * Repositorio JPA para la entidad {@link Favorite}.
 * Proporciona operaciones CRUD y consultas personalizadas para favoritos.
 */
public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA Y CONSULTAS PERSONALIZADAS
    // ============================================================

    /**
     * Devuelve los favoritos del usuario por su ID.
     */
    List<Favorite> findByUserId(Long userId);

    /**
     * Devuelve los favoritos del usuario por su nombre de usuario.
     */
    List<Favorite> findByUserUsername(String username);

    /**
     * Busca un favorito concreto por ID de usuario y de cortometraje.
     */
    Optional<Favorite> findByUserIdAndCortometrajeId(Long userId, Long cortometrajeId);

    /**
     * Devuelve todos los favoritos asociados a un cortometraje.
     */
    List<Favorite> findByCortometrajeId(Long cortometrajeId);

    // ============================================================
    // 📦 FILTROS POR ESTADO ACTIVO/NO ELIMINADO
    // ============================================================

    /**
     * Devuelve los favoritos activos y no eliminados de un usuario.
     */
    List<Favorite> findByUserIdAndIsActiveTrueAndIsDeletedFalse(Long userId);
}
