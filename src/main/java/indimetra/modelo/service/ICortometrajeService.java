package indimetra.modelo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.User;

public interface ICortometrajeService extends IGenericoCRUD<Cortometraje, Long> {

    Page<Cortometraje> findAll(Pageable pageable);

    void updateRating(Long id, BigDecimal rating);

    Optional<Cortometraje> findByName(String name);

    List<Cortometraje> findByCategory(String category);

    List<Cortometraje> findByRating(Double rating);

    Optional<Cortometraje> findByTitle(String title);

    List<Cortometraje> findLatestSeries();

    Optional<Cortometraje> findByIdIfOwnerOrAdmin(Long id, User usuario);

}
