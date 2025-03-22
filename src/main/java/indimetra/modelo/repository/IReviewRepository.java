package indimetra.modelo.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import indimetra.modelo.entity.Review;

public interface IReviewRepository extends JpaRepository<Review, Long> {

    //Hay que hacer pruebas para ver si funciona
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.cortometraje.id = :cortometrajeId")
    BigDecimal calcularPromedioRating(@Param("cortometrajeId") Long cortometrajeId);

}
