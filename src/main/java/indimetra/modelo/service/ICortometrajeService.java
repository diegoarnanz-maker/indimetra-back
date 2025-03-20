package indimetra.modelo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import indimetra.modelo.entity.Cortometraje;

public interface ICortometrajeService extends IGenericoCRUD<Cortometraje, Long> {

    // List<CortometrajeDto> findAllWithDto();

    void updateRating(Long id, BigDecimal rating);

    Optional<Cortometraje> findByName(String name);

    List<Cortometraje> findByCategory(String category);

    List<Cortometraje> findByRating(Double rating);

    Optional<Cortometraje> findByTitle(String title);

    List<Cortometraje> findLatestSeries();

}
