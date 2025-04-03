package indimetra.modelo.service.Review;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isReviewOwner'");
    }

    @Override
    public boolean existsByUserAndSeries(Long userId, Long seriesId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByUserAndSeries'");
    }

    public void actualizarRatingCortometraje(Long cortometrajeId) {
        BigDecimal promedio = reviewRepository.calcularPromedioRating(cortometrajeId);
        cortometrajeService.updateRating(cortometrajeId, promedio != null ? promedio : BigDecimal.ZERO);
    }

    @Override
    public boolean existsByUserAndCortometraje(Long userId, Long cortometrajeId) {
        return reviewRepository.existsByUserIdAndCortometrajeId(userId, cortometrajeId);
    }

    @Override
    public Optional<Review> findByIdIfOwnerOrAdmin(Long id, User user) {
        return read(id).filter(review -> {
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));
            boolean isOwner = review.getUser().getId().equals(user.getId());
            return isAdmin || isOwner;
        });
    }

}
