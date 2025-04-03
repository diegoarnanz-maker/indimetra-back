package indimetra.modelo.service.Review;

import java.util.Optional;

import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericoCRUD;

public interface IReviewService extends IGenericoCRUD<Review, Long> {

    // List<ReviewDto> findAllWithDto();

    // List<ReviewDto> findByUserId(Long userId);

    // List<ReviewDto> findBySeriesId(Long seriesId);

    // List<ReviewDto> findByMinRating(Double minRating);

    // List<ReviewDto> findByRatingRange(Double minRating, Double maxRating);

    boolean isReviewOwner(Long reviewId, Long userId);

    boolean existsByUserAndSeries(Long userId, Long seriesId);

    void actualizarRatingCortometraje(Long cortometrajeId);

    boolean existsByUserAndCortometraje(Long userId, Long cortometrajeId);

    Optional<Review> findByIdIfOwnerOrAdmin(Long id, User user);

}
