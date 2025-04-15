package indimetra.modelo.service.Review;

import java.util.List;
import java.util.Optional;

import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Review.Model.ReviewRequestDto;
import indimetra.modelo.service.Review.Model.ReviewResponseDto;

public interface IReviewService extends IGenericDtoService<Review, ReviewRequestDto, ReviewResponseDto, Long> {

    boolean isReviewOwner(Long reviewId, Long userId);

    boolean existsByUserAndSeries(Long userId, Long seriesId);

    void actualizarRatingCortometraje(Long cortometrajeId);

    boolean existsByUserAndCortometraje(Long userId, Long cortometrajeId);

    Optional<Review> findByIdIfOwnerOrAdmin(Long id, User user);

    ReviewResponseDto createWithValidation(ReviewRequestDto dto, String username);

    ReviewResponseDto updateIfOwnerOrAdmin(Long id, ReviewRequestDto dto, String username);

    void deleteIfOwnerOrAdmin(Long id, String username);

    List<ReviewResponseDto> findAllByUsername(String username);

    List<ReviewResponseDto> findAllByCortometrajeId(Long cortometrajeId);
}
