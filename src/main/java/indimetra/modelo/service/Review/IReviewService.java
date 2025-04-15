package indimetra.modelo.service.Review;

import java.util.List;
import java.util.Optional;

import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Review.Model.ReviewRequestDto;
import indimetra.modelo.service.Review.Model.ReviewResponseDto;

/**
 * Servicio de gestión de reseñas para cortometrajes.
 */
public interface IReviewService extends IGenericDtoService<Review, ReviewRequestDto, ReviewResponseDto, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Verifica si una reseña pertenece a un usuario específico.
     *
     * @param reviewId ID de la reseña
     * @param userId   ID del usuario
     * @return true si el usuario es el dueño de la reseña
     */
    boolean isReviewOwner(Long reviewId, Long userId);

    /**
     * Comprueba si un usuario ya ha reseñado un cortometraje.
     *
     * @param userId         ID del usuario
     * @param cortometrajeId ID del cortometraje
     * @return true si ya existe una reseña de ese usuario para ese cortometraje
     */
    boolean existsByUserAndCortometraje(Long userId, Long cortometrajeId);

    /**
     * Obtiene una reseña si el usuario es dueño o tiene rol de administrador.
     *
     * @param id   ID de la reseña
     * @param user Usuario autenticado
     * @return Optional con la reseña si tiene permisos
     */
    Optional<Review> findByIdIfOwnerOrAdmin(Long id, User user);

    /**
     * Devuelve todas las reseñas realizadas por un usuario.
     *
     * @param username nombre de usuario
     * @return lista de reseñas
     */
    List<ReviewResponseDto> findAllByUsername(String username);

    /**
     * Devuelve todas las reseñas asociadas a un cortometraje.
     *
     * @param cortometrajeId ID del cortometraje
     * @return lista de reseñas
     */
    List<ReviewResponseDto> findAllByCortometrajeId(Long cortometrajeId);

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================

    /**
     * Recalcula y actualiza el rating promedio de un cortometraje.
     *
     * @param cortometrajeId ID del cortometraje
     */
    void actualizarRatingCortometraje(Long cortometrajeId);

    /**
     * Crea una nueva reseña, validando que el usuario no haya reseñado antes.
     *
     * @param dto      datos de la reseña
     * @param username nombre del usuario autenticado
     * @return DTO con la reseña creada
     */
    ReviewResponseDto createWithValidation(ReviewRequestDto dto, String username);

    /**
     * Actualiza una reseña si el usuario es el propietario o tiene rol admin.
     *
     * @param id       ID de la reseña
     * @param dto      datos nuevos
     * @param username usuario autenticado
     * @return DTO actualizado
     */
    ReviewResponseDto updateIfOwnerOrAdmin(Long id, ReviewRequestDto dto, String username);

    // ============================================================
    // 🗑️ ELIMINACIÓN Y RESTAURACIÓN
    // ============================================================

    /**
     * Elimina una reseña si el usuario es el dueño o tiene permisos de
     * administrador.
     *
     * @param id       ID de la reseña
     * @param username nombre del usuario autenticado
     */
    void deleteIfOwnerOrAdmin(Long id, String username);
}
