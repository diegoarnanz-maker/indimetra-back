package indimetra.modelo.service;

import java.util.List;
import java.util.Optional;

import indimetra.modelo.entity.Cortometraje;

public interface ICortometrajeService extends IGenericoCRUD<Cortometraje, Long> {

    // List<CortometrajeDto> findAllWithDto();

    Optional<Cortometraje> findByName(String name);

    List<Cortometraje> findByCategory(String category);

    List<Cortometraje> findByRating(Double rating);

    Optional<Cortometraje> findByTitle(String title);
    
    List<Cortometraje> findLatestSeries();
    
}
