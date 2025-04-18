package indimetra.modelo.service.Cortometraje.helper;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.repository.ICortometrajeRepository;
import indimetra.modelo.repository.IReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Servicio auxiliar para la gestión del rating de los cortometrajes.
 * Se encarga de recalcular y actualizar la valoración promedio
 * basada en las reseñas activas y no eliminadas.
 */
@Service
public class RatingService {

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private ICortometrajeRepository cortometrajeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Recalcula el rating promedio de un cortometraje y lo actualiza en la base de
     * datos.
     * Solo se consideran las reseñas activas y no eliminadas.
     *
     * @param cortometrajeId ID del cortometraje cuyo rating se actualizará
     */
    @Transactional
    public void actualizarRatingCortometraje(Long cortometrajeId) {
        BigDecimal promedio = reviewRepository.calcularPromedioRating(cortometrajeId);
        BigDecimal nuevoRating = (promedio != null) ? promedio : BigDecimal.ZERO;

        cortometrajeRepository.updateRating(cortometrajeId, nuevoRating);
        entityManager.flush();
    }
}
