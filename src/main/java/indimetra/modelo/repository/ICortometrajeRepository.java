package indimetra.modelo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import indimetra.modelo.entity.Category;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.User;

/**
 * Repositorio JPA para la entidad {@link Cortometraje}.
 * Proporciona operaciones CRUD y consultas personalizadas para cortometrajes.
 */
public interface ICortometrajeRepository extends JpaRepository<Cortometraje, Long> {

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================

    /**
     * Actualiza el rating de un cortometraje por su ID.
     *
     * @param id     ID del cortometraje
     * @param rating nuevo valor de rating
     */
    @Modifying
    @Transactional
    @Query("UPDATE Cortometraje c SET c.rating = :rating WHERE c.id = :id")
    void updateRating(@Param("id") Long id, @Param("rating") BigDecimal rating);

    // ============================================================
    // 🔍 BÚSQUEDAS PERSONALIZADAS (sin filtros de estado)
    // ============================================================

    List<Cortometraje> findByRatingGreaterThanEqual(BigDecimal rating);

    List<Cortometraje> findTop5ByOrderByCreatedAtDesc();

    List<Cortometraje> findTop5ByOrderByRatingDesc();

    List<Cortometraje> findByDurationLessThanEqual(Integer duration);

    List<Cortometraje> findByUser(User user);

    @Query("SELECT DISTINCT c.language FROM Cortometraje c")
    List<String> findDistinctLanguages();

    /**
     * Busca cortometrajes por título (sin importar mayúsculas/minúsculas).
     */
    @Query("SELECT c FROM Cortometraje c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Cortometraje> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Busca cortometrajes por nombre de categoría (sin importar
     * mayúsculas/minúsculas).
     */
    @Query("SELECT c FROM Cortometraje c WHERE LOWER(c.category.name) = LOWER(:categoryName)")
    List<Cortometraje> findByCategoryNameIgnoreCase(@Param("categoryName") String categoryName);

    // ============================================================
    // 🔍 BÚSQUEDAS CON FILTRO ACTIVO/NO ELIMINADO
    // ============================================================

    List<Cortometraje> findByIsActiveTrueAndIsDeletedFalse();

    Page<Cortometraje> findByIsActiveTrueAndIsDeletedFalse(Pageable pageable);

    List<Cortometraje> findByUserAndIsActiveTrueAndIsDeletedFalse(User user);

    List<Cortometraje> findByTitleContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String title);

    List<Cortometraje> findByCategoryNameIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String categoryName);

    List<Cortometraje> findByLanguageIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String language);

    // ============================================================
    // VISIBILIDAD (autores activos y no eliminados)
    // ============================================================

    /**
     * Devuelve los cortometrajes visibles (activos, no eliminados y con autor
     * activo).
     */
    @Query("""
                SELECT c FROM Cortometraje c
                WHERE c.isActive = true
                AND c.isDeleted = false
                AND c.user.isActive = true
                AND c.user.isDeleted = false
            """)
    List<Cortometraje> findAllVisible();

    /**
     * Devuelve los cortometrajes visibles (versión paginada).
     */
    @Query("""
                SELECT c FROM Cortometraje c
                WHERE c.isActive = true
                AND c.isDeleted = false
                AND c.user.isActive = true
                AND c.user.isDeleted = false
            """)
    Page<Cortometraje> findAllVisible(Pageable pageable);

    @Query("""
                SELECT c FROM Cortometraje c
                WHERE c.isActive = true AND c.isDeleted = false
                  AND (:genero IS NULL OR LOWER(c.category.name) = LOWER(:genero))
                  AND (:idioma IS NULL OR LOWER(c.language) = LOWER(:idioma))
                  AND (:duracion IS NULL OR
                      (:duracion = '< 5 min' AND c.duration < 5) OR
                      (:duracion = '5-10 min' AND c.duration BETWEEN 5 AND 10) OR
                      (:duracion = '10-20 min' AND c.duration BETWEEN 11 AND 20) OR
                      (:duracion = '> 20 min' AND c.duration > 20)
                  )
            """)
    Page<Cortometraje> buscarConFiltros(
            @Param("genero") String genero,
            @Param("idioma") String idioma,
            @Param("duracion") String duracion,
            Pageable pageable);

    // ============================================================
    // ❓ VERIFICACIONES DE EXISTENCIA
    // ============================================================

    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndIsDeletedFalse(Long userId);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    boolean existsByCategory(Category category);

}
