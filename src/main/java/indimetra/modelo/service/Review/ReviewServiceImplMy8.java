package indimetra.modelo.service.Review;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.IReviewRepository;
import indimetra.modelo.repository.IUserRepository;
import indimetra.modelo.service.Base.GenericDtoServiceImpl;
import indimetra.modelo.service.Cortometraje.ICortometrajeService;
import indimetra.modelo.service.Review.Model.ReviewRequestDto;
import indimetra.modelo.service.Review.Model.ReviewResponseDto;
import indimetra.modelo.service.User.IUserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class ReviewServiceImplMy8
        extends GenericDtoServiceImpl<Review, ReviewRequestDto, ReviewResponseDto, Long>
        implements IReviewService {

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICortometrajeService cortometrajeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private Validator validator;

    @Override
    protected IReviewRepository getRepository() {
        return reviewRepository;
    }

    @Override
    protected Class<Review> getEntityClass() {
        return Review.class;
    }

    @Override
    protected Class<ReviewRequestDto> getRequestDtoClass() {
        return ReviewRequestDto.class;
    }

    @Override
    protected Class<ReviewResponseDto> getResponseDtoClass() {
        return ReviewResponseDto.class;
    }

    @Override
    protected void setEntityId(Review entity, Long id) {
        entity.setId(id);
    }

    // Métodos personalizados

    @Override
    public boolean isReviewOwner(Long reviewId, Long userId) {
        return read(reviewId)
                .map(review -> review.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException("Review no encontrada"));
    }

    @Override
    public boolean existsByUserAndSeries(Long userId, Long seriesId) {
        throw new UnsupportedOperationException("No implementado");
    }

    @Override
    public boolean existsByUserAndCortometraje(Long userId, Long cortometrajeId) {

        return reviewRepository.existsByUserIdAndCortometrajeIdAndIsDeletedFalse(userId, cortometrajeId);
    }

    @Override
    public Optional<Review> findByIdIfOwnerOrAdmin(Long id, User user) {
        Review review = read(id)
                .orElseThrow(() -> new NotFoundException("Review no encontrada con ID: " + id));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));
        boolean isOwner = review.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("No tienes permiso para acceder a esta review");
        }

        return Optional.of(review);
    }

    @Override
    public void actualizarRatingCortometraje(Long cortometrajeId) {
        if (cortometrajeId == null || cortometrajeId <= 0) {
            throw new BadRequestException("ID de cortometraje inválido");
        }

        BigDecimal promedio = reviewRepository.calcularPromedioRating(cortometrajeId);
        cortometrajeService.updateRating(cortometrajeId, promedio != null ? promedio : BigDecimal.ZERO);
    }

    @Override
    public ReviewResponseDto createWithValidation(ReviewRequestDto dto, String username) {
        Set<ConstraintViolation<ReviewRequestDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String mensajeError = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException("Errores de validación: " + mensajeError);
        }

        try {
            var idField = dto.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(dto);
            if (idValue != null) {
                throw new BadRequestException("No debes enviar el campo 'id' al crear una reseña");
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        var cortometraje = cortometrajeService.read(dto.getCortometrajeId())
                .orElseThrow(
                        () -> new NotFoundException("Cortometraje no encontrado con ID: " + dto.getCortometrajeId()));

        if (existsByUserAndCortometraje(user.getId(), cortometraje.getId())) {
            throw new BadRequestException("Ya has realizado una reseña para este cortometraje");
        }

        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setUser(user);
        review.setCortometraje(cortometraje);

        Review saved = reviewRepository.save(review);
        actualizarRatingCortometraje(cortometraje.getId());

        return ReviewResponseDto.builder()
                .id(saved.getId())
                .rating(saved.getRating())
                .comment(saved.getComment())
                .username(user.getUsername())
                .cortometrajeId(cortometraje.getId())
                .cortometrajeTitle(cortometraje.getTitle())
                .build();
    }

    @Override
    public ReviewResponseDto updateIfOwnerOrAdmin(Long id, ReviewRequestDto dto, String username) {
        Set<ConstraintViolation<ReviewRequestDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String mensajeError = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new BadRequestException("Errores de validación: " + mensajeError);
        }

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        Review review = findByIdIfOwnerOrAdmin(id, user)
                .orElseThrow(() -> new ForbiddenException("No tienes permisos para actualizar esta reseña"));

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review updated = reviewRepository.save(review);
        actualizarRatingCortometraje(updated.getCortometraje().getId());

        return ReviewResponseDto.builder()
                .id(updated.getId())
                .rating(updated.getRating())
                .comment(updated.getComment())
                .username(updated.getUser().getUsername())
                .cortometrajeId(updated.getCortometraje().getId())
                .cortometrajeTitle(updated.getCortometraje().getTitle())
                .build();
    }

    @Override
    public void deleteIfOwnerOrAdmin(Long id, String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        Review review = read(id)
                .orElseThrow(() -> new NotFoundException("Review no encontrada con ID: " + id));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));
        boolean isOwner = review.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("No tienes permisos para eliminar esta reseña");
        }

        reviewRepository.deleteById(id);
        actualizarRatingCortometraje(review.getCortometraje().getId());
    }

    @Override
    public List<ReviewResponseDto> findAllByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        return reviewRepository.findByUserIdAndIsDeletedFalse(user.getId()).stream()
                .filter(r -> r.getCortometraje() != null &&
                        r.getCortometraje().getIsActive() &&
                        !r.getCortometraje().getIsDeleted() &&
                        r.getCortometraje().getUser().getIsActive())
                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                .toList();
    }

    @Override
    public ReviewResponseDto findById(Long id) {
        Review review = readEntityById(id);

        if (review.getIsDeleted()
                || review.getCortometraje() == null
                || review.getCortometraje().getIsDeleted()
                || !review.getCortometraje().getIsActive()
                || !review.getCortometraje().getUser().getIsActive()) {
            throw new NotFoundException("Reseña no disponible o el cortometraje está desactivado");
        }

        return ReviewResponseDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .username(review.getUser().getUsername())
                .cortometrajeId(review.getCortometraje().getId())
                .cortometrajeTitle(review.getCortometraje().getTitle())
                .build();
    }

}
