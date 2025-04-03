package indimetra.modelo.service.Cortometraje;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericoCRUD;

public interface ICortometrajeService extends IGenericoCRUD<Cortometraje, Long> {

    Page<Cortometraje> findAll(Pageable pageable);

    void updateRating(Long id, BigDecimal rating);

    List<Cortometraje> findByCategory(String category);

    List<Cortometraje> findByRating(Double rating);

    List<Cortometraje> findByTitleContainingIgnoreCase(@Param("title") String title);

    List<Cortometraje> findLatestSeries();

    Optional<Cortometraje> findByIdIfOwnerOrAdmin(Long id, User usuario);

    List<Cortometraje> findTopRated();

    List<Cortometraje> findByDuracionMenorOIgual(Integer minutos);

}
