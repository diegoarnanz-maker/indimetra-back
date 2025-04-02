package indimetra.modelo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.ICortometrajeRepository;

@Service
public class CortometrajeServiceImplMy8 extends GenericoCRUDServiceImplMy8<Cortometraje, Long>
        implements ICortometrajeService {

    @Autowired
    private ICortometrajeRepository cortometrajeRepository;

    @Override
    protected ICortometrajeRepository getRepository() {
        return cortometrajeRepository;
    }

    @Override
    public List<Cortometraje> findByCategory(String categoryName) {
        return cortometrajeRepository.findByCategoryNameIgnoreCase(categoryName);
    }

    @Override
    public List<Cortometraje> findByRating(Double rating) {
        return cortometrajeRepository.findByRatingGreaterThanEqual(BigDecimal.valueOf(rating));
    }

    @Override
    public List<Cortometraje> findByTitleContainingIgnoreCase(String title) {
        return cortometrajeRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Cortometraje> findLatestSeries() {
        return cortometrajeRepository.findTop5ByOrderByCreatedAtDesc();
    }

    @Override
    public void updateRating(Long id, BigDecimal rating) {
        cortometrajeRepository.updateRating(id, rating);
    }

    @Override
    public Optional<Cortometraje> findByIdIfOwnerOrAdmin(Long id, User usuario) {
        Optional<Cortometraje> optional = this.read(id);

        if (optional.isPresent()) {
            Cortometraje cortometraje = optional.get();

            boolean esPropietario = cortometraje.getUser().getId().equals(usuario.getId());
            boolean esAdmin = usuario.getRoles().stream()
                    .anyMatch(r -> r.getName().equals(Role.RoleType.ROLE_ADMIN));

            if (esPropietario || esAdmin) {
                return Optional.of(cortometraje);
            }
        }

        return Optional.empty();
    }

    @Override
    public Page<Cortometraje> findAll(Pageable pageable) {
        return cortometrajeRepository.findAll(pageable);
    }

    @Override
    public List<Cortometraje> findTopRated() {
        return cortometrajeRepository.findTop5ByOrderByRatingDesc();
    }

    @Override
    public List<Cortometraje> findByDuracionMenorOIgual(Integer minutos) {
        return cortometrajeRepository.findByDurationLessThanEqual(minutos);
    }

}
