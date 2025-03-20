package indimetra.modelo.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import indimetra.modelo.entity.Cortometraje;

public interface ICortometrajeRepository extends JpaRepository<Cortometraje, Long> {

    //Hay que hacer pruebas para ver si funciona
    @Modifying
    @Query("UPDATE Cortometraje c SET c.rating = :rating WHERE c.id = :id")
    void updateRating(@Param("id") Long id, @Param("rating") BigDecimal rating);

}
