package indimetra.modelo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.User;

public interface ICortometrajeRepository extends JpaRepository<Cortometraje, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Cortometraje c SET c.rating = :rating WHERE c.id = :id")
    void updateRating(@Param("id") Long id, @Param("rating") BigDecimal rating);

    @Query("SELECT c FROM Cortometraje c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Cortometraje> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT c FROM Cortometraje c WHERE LOWER(c.category.name) = LOWER(:categoryName)")
    List<Cortometraje> findByCategoryNameIgnoreCase(@Param("categoryName") String categoryName);

    List<Cortometraje> findByRatingGreaterThanEqual(BigDecimal rating);

    List<Cortometraje> findTop5ByOrderByCreatedAtDesc();

    List<Cortometraje> findTop5ByOrderByRatingDesc();

    List<Cortometraje> findByDurationLessThanEqual(Integer duration);

    boolean existsByUserId(Long userId);

    List<Cortometraje> findByUser(User user);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

}
