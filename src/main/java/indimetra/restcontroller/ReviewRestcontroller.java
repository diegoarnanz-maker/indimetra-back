package indimetra.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indimetra.modelo.dto.ReviewRequestDto;
import indimetra.modelo.dto.ReviewResponseDto;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.ICortometrajeService;
import indimetra.modelo.service.IReviewService;
import indimetra.modelo.service.IUserService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/review")
public class ReviewRestcontroller {

        @Autowired
        private IReviewService reviewService;

        @Autowired
        private IUserService userService;

        @Autowired
        private ICortometrajeService cortometrajeService;

        @Autowired
        private ModelMapper modelMapper;

        @GetMapping
        public ResponseEntity<List<ReviewResponseDto>> findAll() {
                List<Review> reviews = reviewService.findAll();

                List<ReviewResponseDto> response = reviews.stream()
                                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                                .collect(Collectors.toList());

                return ResponseEntity.status(200).body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ReviewResponseDto> findById(@PathVariable Long id) {
                Review review = reviewService.read(id)
                                .orElseThrow(() -> new RuntimeException("Review no encontrada"));

                ReviewResponseDto response = modelMapper.map(review, ReviewResponseDto.class);

                return ResponseEntity.status(200).body(response);
        }

        @PostMapping
        public ResponseEntity<ReviewResponseDto> create(
                        @RequestBody @Valid ReviewRequestDto dto,
                        Authentication authentication) {

                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuario no encontrado: " + authentication.getName()));

                Cortometraje cortometraje = cortometrajeService.read(dto.getCortometrajeId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Cortometraje no encontrado con ID: " + dto.getCortometrajeId()));

                if (reviewService.existsByUserAndCortometraje(user.getId(), cortometraje.getId())) {
                        throw new RuntimeException("Ya has realizado una reseña para este cortometraje.");
                }

                Review review = modelMapper.map(dto, Review.class);
                review.setUser(user);
                review.setCortometraje(cortometraje);
                review.setId(null);

                Review saved = reviewService.create(review);

                reviewService.actualizarRatingCortometraje(cortometraje.getId());

                ReviewResponseDto response = modelMapper.map(saved, ReviewResponseDto.class);
                response.setUsername(user.getUsername());
                response.setCortometrajeId(cortometraje.getId());
                response.setCortometrajeTitle(cortometraje.getTitle());

                return ResponseEntity.status(201).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ReviewResponseDto> update(
                        @PathVariable Long id,
                        @RequestBody @Valid ReviewRequestDto dto,
                        Authentication authentication) {

                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Review review = reviewService.findByIdIfOwnerOrAdmin(id, user)
                                .orElseThrow(() -> new RuntimeException(
                                                "No tienes permisos para modificar esta reseña"));

                review.setRating(dto.getRating());
                review.setComment(dto.getComment());

                Review updated = reviewService.update(review);
                reviewService.actualizarRatingCortometraje(review.getCortometraje().getId());

                ReviewResponseDto response = modelMapper.map(updated, ReviewResponseDto.class);
                response.setUsername(review.getUser().getUsername());
                response.setCortometrajeId(review.getCortometraje().getId());
                response.setCortometrajeTitle(review.getCortometraje().getTitle());

                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Review review = reviewService.findByIdIfOwnerOrAdmin(id, user)
                                .orElseThrow(() -> new RuntimeException(
                                                "No tienes permisos para eliminar esta reseña"));

                reviewService.delete(id);
                reviewService.actualizarRatingCortometraje(review.getCortometraje().getId());

                return ResponseEntity.noContent().build();
        }

}
