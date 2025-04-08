package indimetra.modelo.service.Review;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.IReviewRepository;
import indimetra.modelo.service.Base.GenericoCRUDServiceImplMy8;
import indimetra.modelo.service.Cortometraje.ICortometrajeService;

@Service
public class ReviewServiceImplMy8 extends GenericoCRUDServiceImplMy8<Review, Long> implements IReviewService {

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private ICortometrajeService cortometrajeService;

    @Override
    protected IReviewRepository getRepository() {
        return reviewRepository;
    }

    @Override
    public boolean isReviewOwner(Long reviewId, Long userId) {
        if (reviewId == null || userId == null) {
            throw new BadRequestException("El ID de la review y del usuario no pueden ser nulos");
        }

        return read(reviewId)
                .map(review -> review.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException("La review no fue encontrada"));
    }

    @Override
    public boolean existsByUserAndSeries(Long userId, Long seriesId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByUserAndSeries'");
    }

    @Override
    public void actualizarRatingCortometraje(Long cortometrajeId) {
        if (cortometrajeId == null || cortometrajeId <= 0) {
            throw new BadRequestException("El ID del cortometraje no es válido");
        }

        BigDecimal promedio = reviewRepository.calcularPromedioRating(cortometrajeId);
        cortometrajeService.updateRating(cortometrajeId, promedio != null ? promedio : BigDecimal.ZERO);
    }

    @Override
    public boolean existsByUserAndCortometraje(Long userId, Long cortometrajeId) {
        if (userId == null || userId <= 0 || cortometrajeId == null || cortometrajeId <= 0) {
            throw new BadRequestException("Los IDs del usuario y del cortometraje deben ser válidos y mayores que 0");
        }

        return reviewRepository.existsByUserIdAndCortometrajeId(userId, cortometrajeId);
    }

    @Override
    public Optional<Review> findByIdIfOwnerOrAdmin(Long id, User user) {
        Review review = read(id)
                .orElseThrow(() -> new NotFoundException("Review no encontrada con ID: " + id));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));
        boolean isOwner = review.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("No tienes permiso para acceder a esta review");
        }

        return Optional.of(review);
    }

}
