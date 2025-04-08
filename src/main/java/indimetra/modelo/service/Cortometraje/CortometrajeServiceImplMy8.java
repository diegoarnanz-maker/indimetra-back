package indimetra.modelo.service.Cortometraje;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.ICortometrajeRepository;
import indimetra.modelo.service.Base.GenericoCRUDServiceImplMy8;

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
        if (rating == null || rating < 0) {
            throw new BadRequestException("El rating debe ser mayor o igual que 0");
        }

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
        Cortometraje cortometraje = this.read(id)
                .orElseThrow(() -> new NotFoundException("Cortometraje no encontrado"));

        boolean esPropietario = cortometraje.getUser().getId().equals(usuario.getId());
        boolean esAdmin = usuario.getRoles().stream()
                .anyMatch(r -> r.getName().equals(Role.RoleType.ROLE_ADMIN));

        if (esPropietario || esAdmin) {
            return Optional.of(cortometraje);
        }

        throw new ForbiddenException("No tienes permisos para acceder a este cortometraje");
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
        if (minutos == null || minutos <= 0) {
            throw new BadRequestException("La duración debe ser mayor que 0");
        }

        return cortometrajeRepository.findByDurationLessThanEqual(minutos);
    }

}
