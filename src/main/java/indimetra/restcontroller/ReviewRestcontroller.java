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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Review Controller", description = "Gestión de reseñas de cortometrajes")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/review")
public class ReviewRestcontroller extends BaseRestcontroller {

        @Autowired
        private IReviewService reviewService;

        // ============================================
        // 🔓 ZONA PÚBLICA (sin autenticación)
        // ============================================

        // 🔹 LECTURA

        @Operation(summary = "Obtener todas las reseñas")
        @GetMapping
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findAll() {
                List<ReviewResponseDto> response = reviewService.findAll();
                return success(response, "Listado de reseñas");
        }

        @Operation(summary = "Obtener una reseña por su ID")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> findById(@PathVariable Long id) {
                ReviewResponseDto response = reviewService.findById(id);
                return success(response, "Detalle de la reseña");
        }

        @Operation(summary = "Obtener reseñas por nombre de usuario")
        @GetMapping("/buscar/por-usuario/{username}")
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findByUsername(@PathVariable String username) {
                List<ReviewResponseDto> response = reviewService.findAllByUsername(username);
                return success(response, "Reseñas del usuario: " + username);
        }

        @Operation(summary = "Obtener reseñas por ID del cortometraje")
        @GetMapping("/buscar/por-cortometraje/{cortometrajeId}")
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findByCortometraje(
                        @PathVariable Long cortometrajeId) {
                List<ReviewResponseDto> response = reviewService.findAllByCortometrajeId(cortometrajeId);
                return success(response, "Reseñas del cortometraje ID: " + cortometrajeId);
        }

        // ============================================
        // 👤 ZONA AUTENTICADO (ROLE_USER o ROLE_ADMIN)
        // ============================================

        // 🔹 LECTURA

        @Operation(summary = "Obtener reseñas del usuario autenticado")
        @PreAuthorize("hasAuthority('ROLE_USER')")
        @GetMapping("/mis-reviews")
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> findMyReviews() {
                List<ReviewResponseDto> response = reviewService.findAllByUsername(getUsername());
                return success(response, "Reseñas del usuario autenticado");
        }

        // 🔹 GESTIÓN

        @Operation(summary = "Crear una nueva reseña")
        @PreAuthorize("hasAuthority('ROLE_USER')")
        @PostMapping
        public ResponseEntity<ApiResponse<ReviewResponseDto>> create(@RequestBody @Valid ReviewRequestDto dto) {
                ReviewResponseDto response = reviewService.createWithValidation(dto, getUsername());
                return created(response, "Reseña creada correctamente");
        }

        @Operation(summary = "Actualizar una reseña (dueño o admin)")
        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<ReviewResponseDto>> update(@PathVariable Long id,
                        @RequestBody @Valid ReviewRequestDto dto) {
                ReviewResponseDto response = reviewService.updateIfOwnerOrAdmin(id, dto, getUsername());
                return success(response, "Reseña actualizada correctamente");
        }

        @Operation(summary = "Eliminar una reseña (dueño o admin)")
        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
                reviewService.deleteIfOwnerOrAdmin(id, getUsername());
                return success(null, "Reseña eliminada correctamente");
        }
}
