package indimetra.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Review.IReviewService;
import indimetra.modelo.service.Review.Model.ReviewRequestDto;
import indimetra.modelo.service.Review.Model.ReviewResponseDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/review")
public class ReviewRestcontroller extends BaseRestcontroller {

        @Autowired
        private IReviewService reviewService;

        @PreAuthorize("isAuthenticated()")
        @GetMapping
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findAll() {
                List<ReviewResponseDto> response = reviewService.findAll();
                return success(response, "Listado de reseñas");
        }

        @PreAuthorize("hasAuthority('ROLE_USER')")
        @GetMapping("/mis-reviews")
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findMyReviews() {
                List<ReviewResponseDto> response = reviewService.findAllByUsername(getUsername());
                return success(response, "Reseñas del usuario autenticado");
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> findById(@PathVariable Long id) {
                ReviewResponseDto response = reviewService.findById(id);
                return success(response, "Detalle de la reseña");
        }

        @PreAuthorize("hasAuthority('ROLE_USER')")
        @PostMapping
        public ResponseEntity<ApiResponse<ReviewResponseDto>> create(@RequestBody @Valid ReviewRequestDto dto) {
                ReviewResponseDto response = reviewService.createWithValidation(dto, getUsername());
                return created(response, "Reseña creada correctamente");
        }

        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> update(@PathVariable Long id,
                        @RequestBody @Valid ReviewRequestDto dto) {
                ReviewResponseDto response = reviewService.updateIfOwnerOrAdmin(id, dto, getUsername());
                return success(response, "Reseña actualizada correctamente");
        }

        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
                reviewService.deleteIfOwnerOrAdmin(id, getUsername());
                return success(null, "Reseña eliminada correctamente");
        }
}
