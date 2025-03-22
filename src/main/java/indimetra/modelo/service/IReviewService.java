package indimetra.modelo.service;

import indimetra.modelo.entity.Review;

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

}
