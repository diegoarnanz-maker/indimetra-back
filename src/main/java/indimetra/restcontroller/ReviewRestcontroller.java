package indimetra.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import indimetra.exception.NotFoundException;
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

        @GetMapping
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findAll() {
                List<ReviewResponseDto> response = reviewService.findAll();
                return success(response, "Listado de reseñas");
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> findById(@PathVariable Long id) {
                ReviewResponseDto response = reviewService.findById(id);
                return success(response, "Detalle de la reseña");
        }

        @PostMapping
        public ResponseEntity<ApiResponse<ReviewResponseDto>> create(@RequestBody @Valid ReviewRequestDto dto) {
                ReviewResponseDto response = reviewService.createWithValidation(dto, getUsername());
                return created(response, "Reseña creada correctamente");
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> update(@PathVariable Long id,
                        @RequestBody @Valid ReviewRequestDto dto) {
                ReviewResponseDto response = reviewService.updateIfOwnerOrAdmin(id, dto, getUsername());
                return success(response, "Reseña actualizada correctamente");
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
                reviewService.deleteIfOwnerOrAdmin(id, getUsername());
                return success(null, "Reseña eliminada correctamente");
        }
}
