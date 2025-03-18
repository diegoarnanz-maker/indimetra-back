package indimetra.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Review;

public interface IReviewRepository extends JpaRepository<Review, Long> {

}
