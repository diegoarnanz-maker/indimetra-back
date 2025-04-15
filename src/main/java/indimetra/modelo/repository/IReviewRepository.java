package indimetra.modelo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import indimetra.modelo.entity.Review;

public interface IReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.cortometraje.id = :cortometrajeId AND r.isActive = true AND r.isDeleted = false")
    BigDecimal calcularPromedioRating(@Param("cortometrajeId") Long cortometrajeId);

    boolean existsByUserIdAndCortometrajeId(Long userId, Long cortometrajeId);

    List<Review> findByUserIdAndIsDeletedFalse(Long userId);

    // Filtrados por isActive e isDeleted
    List<Review> findByIsActiveTrueAndIsDeletedFalse();

    List<Review> findByUserIdAndIsActiveTrueAndIsDeletedFalse(Long userId);

    List<Review> findByCortometrajeIdAndIsActiveTrueAndIsDeletedFalse(Long cortometrajeId);

    List<Review> findByCortometrajeId(Long cortometrajeId);

    boolean existsByUserIdAndCortometrajeIdAndIsDeletedFalse(Long userId, Long cortometrajeId);

    List<Review> findByIsDeletedFalse();

    List<Review> findByCortometrajeIdAndIsDeletedFalse(Long cortometrajeId);

}
