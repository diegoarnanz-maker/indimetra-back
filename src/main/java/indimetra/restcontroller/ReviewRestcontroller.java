package indimetra.restcontroller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import indimetra.exception.BadRequestException;
import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Cortometraje.ICortometrajeService;
import indimetra.modelo.service.Review.IReviewService;
import indimetra.modelo.service.Review.Model.ReviewRequestDto;
import indimetra.modelo.service.Review.Model.ReviewResponseDto;
import indimetra.modelo.service.User.IUserService;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/review")
public class ReviewRestcontroller extends BaseRestcontroller {

        @Autowired
        private IReviewService reviewService;

        @Autowired
        private IUserService userService;

        @Autowired
        private ICortometrajeService cortometrajeService;

        @Autowired
        private ModelMapper modelMapper;

        @GetMapping
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findAll() {
                List<ReviewResponseDto> response = reviewService.findAll().stream()
                                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                                .toList();

                return success(response, "Listado de reseñas");
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> findById(@PathVariable Long id) {
                Review review = reviewService.read(id)
                                .orElseThrow(() -> new NotFoundException("Reseña no encontrada"));

                ReviewResponseDto response = modelMapper.map(review, ReviewResponseDto.class);
                return success(response, "Detalle de la reseña");
        }

        @PostMapping
        public ResponseEntity<ApiResponse<ReviewResponseDto>> create(
                        @RequestBody @Valid ReviewRequestDto dto,
                        Authentication authentication) {

                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                Cortometraje cortometraje = cortometrajeService.read(dto.getCortometrajeId())
                                .orElseThrow(() -> new NotFoundException("Cortometraje no encontrado"));

                if (reviewService.existsByUserAndCortometraje(user.getId(), cortometraje.getId())) {
                        throw new BadRequestException("Ya has realizado una reseña para este cortometraje");
                }

                Review review = modelMapper.map(dto, Review.class);
                review.setUser(user);
                review.setCortometraje(cortometraje);

                Review saved = reviewService.create(review);
                reviewService.actualizarRatingCortometraje(cortometraje.getId());

                ReviewResponseDto response = modelMapper.map(saved, ReviewResponseDto.class);
                response.setUsername(user.getUsername());
                response.setCortometrajeId(cortometraje.getId());
                response.setCortometrajeTitle(cortometraje.getTitle());

                return created(response, "Reseña creada correctamente");
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> update(
                        @PathVariable Long id,
                        @RequestBody @Valid ReviewRequestDto dto,
                        Authentication authentication) {

                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                Review review = reviewService.findByIdIfOwnerOrAdmin(id, user)
                                .orElseThrow(() -> new ForbiddenException(
                                                "No tienes permisos para modificar esta reseña"));

                review.setRating(dto.getRating());
                review.setComment(dto.getComment());

                Review updated = reviewService.update(review);
                reviewService.actualizarRatingCortometraje(updated.getCortometraje().getId());

                ReviewResponseDto response = modelMapper.map(updated, ReviewResponseDto.class);
                response.setUsername(updated.getUser().getUsername());
                response.setCortometrajeId(updated.getCortometraje().getId());
                response.setCortometrajeTitle(updated.getCortometraje().getTitle());

                return success(response, "Reseña actualizada correctamente");
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication authentication) {
                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                Review review = reviewService.findByIdIfOwnerOrAdmin(id, user)
                                .orElseThrow(() -> new ForbiddenException(
                                                "No tienes permisos para eliminar esta reseña"));

                reviewService.delete(id);
                reviewService.actualizarRatingCortometraje(review.getCortometraje().getId());

                return success(null, "Reseña eliminada correctamente");
        }
}
