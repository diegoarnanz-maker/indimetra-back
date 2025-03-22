package indimetra.modelo.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Review;
import indimetra.modelo.repository.IReviewRepository;

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

}
