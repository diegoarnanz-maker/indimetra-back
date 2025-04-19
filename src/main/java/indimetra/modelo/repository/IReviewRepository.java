package indimetra.modelo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;

/**
 * Repositorio JPA para la entidad {@link Review}.
 * Proporciona operaciones CRUD y consultas personalizadas para reseñas de
 * cortometrajes.
 */
public interface IReviewRepository extends JpaRepository<Review, Long> {

    // ============================================================
    // 📊 ESTADÍSTICAS
    // ============================================================

    /**
     * Calcula el promedio de rating de las reseñas activas y no eliminadas de un
     * cortometraje.
     *
     * @param cortometrajeId ID del cortometraje
     * @return Promedio de calificaciones
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.cortometraje.id = :cortometrajeId AND r.isActive = true AND r.isDeleted = false")
    BigDecimal calcularPromedioRating(@Param("cortometrajeId") Long cortometrajeId);

    // ============================================================
    // 🔍 EXISTENCIA
    // ============================================================

    /**
     * Verifica si existe una reseña para un cortometraje hecha por un usuario.
     */
    boolean existsByUserIdAndCortometrajeId(Long userId, Long cortometrajeId);

    /**
     * Verifica si existe una reseña activa y no eliminada para un cortometraje
     * hecha por un usuario.
     */
    boolean existsByUserIdAndCortometrajeIdAndIsDeletedFalse(Long userId, Long cortometrajeId);

    // ============================================================
    // 🔍 BÚSQUEDAS GENERALES
    // ============================================================

    /**
     * Devuelve todas las reseñas activas y no eliminadas.
     */
    List<Review> findByIsActiveTrueAndIsDeletedFalse();

    /**
     * Devuelve todas las reseñas no eliminadas.
     */
    List<Review> findByIsDeletedFalse();

    /**
     * Devuelve todas las pasando un objeto User.
     */
    List<Review> findByUser(User user);

    // ============================================================
    // 🔍 BÚSQUEDAS POR USUARIO
    // ============================================================

    /**
     * Devuelve las reseñas activas y no eliminadas de un usuario.
     */
    List<Review> findByUserIdAndIsActiveTrueAndIsDeletedFalse(Long userId);

    /**
     * Devuelve las reseñas no eliminadas de un usuario.
     */
    List<Review> findByUserIdAndIsDeletedFalse(Long userId);

    // ============================================================
    // 🔍 BÚSQUEDAS POR CORTOMETRAJE
    // ============================================================

    /**
     * Devuelve las reseñas activas y no eliminadas de un cortometraje.
     */
    List<Review> findByCortometrajeIdAndIsActiveTrueAndIsDeletedFalse(Long cortometrajeId);

    /**
     * Devuelve todas las reseñas asociadas a un cortometraje.
     */
    List<Review> findByCortometrajeId(Long cortometrajeId);

    /**
     * Devuelve las reseñas no eliminadas de un cortometraje.
     */
    List<Review> findByCortometrajeIdAndIsDeletedFalse(Long cortometrajeId);
}
