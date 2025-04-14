package indimetra.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Cortometraje.ICortometrajeService;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeRequestDto;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeResponseDto;
import indimetra.modelo.service.Shared.Model.PagedResponse;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cortometraje")
public class CortometrajeRestcontroller extends BaseRestcontroller {

        @Autowired
        private ICortometrajeService cortometrajeService;

        @GetMapping
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> findAll() {
                List<CortometrajeResponseDto> response = cortometrajeService.findAll();
                return success(response, "Listado de cortometrajes");
        }

        @GetMapping("/paginated")
        public ResponseEntity<PagedResponse<CortometrajeResponseDto>> findAllPaginated(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                PagedResponse<CortometrajeResponseDto> response = cortometrajeService
                                .findAllPaginated(PageRequest.of(page, size));

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<CortometrajeResponseDto>> findById(@PathVariable Long id) {
                CortometrajeResponseDto response = cortometrajeService.findById(id);
                return success(response, "Cortometraje encontrado");
        }

        @PreAuthorize("hasAuthority('ROLE_USER')")
        @GetMapping("/buscar/mis-cortometrajes")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarMisCortometrajes() {
                List<CortometrajeResponseDto> response = cortometrajeService.findByUsername(getUsername());
                return success(response, "Tus cortometrajes");
        }

        @GetMapping("/buscar/{title}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarPorTitulo(@PathVariable String title) {
                List<CortometrajeResponseDto> response = cortometrajeService.findByTitleContainingIgnoreCase(title);
                return success(response, "Resultados para el título: " + title);
        }

        @GetMapping("/buscar/categoria/{categoryName}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarPorCategoria(
                        @PathVariable String categoryName) {
                List<CortometrajeResponseDto> response = cortometrajeService.findByCategory(categoryName);
                return success(response, "Cortometrajes en la categoría: " + categoryName);
        }

        @GetMapping("/buscar/latest")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> obtenerTop5Nuevos() {
                List<CortometrajeResponseDto> response = cortometrajeService.findLatestSeries();
                return success(response, "Top 5 más recientes");
        }

        @GetMapping("/buscar/rating-minimo/{valor}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarPorMinimoRating(
                        @PathVariable Double valor) {
                List<CortometrajeResponseDto> response = cortometrajeService.findByRating(valor);
                return success(response, "Cortometrajes con rating >= " + valor);
        }

        @GetMapping("/buscar/top5-mejor-valorados")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> obtenerTop5MejorValorados() {
                List<CortometrajeResponseDto> response = cortometrajeService.findTopRated();
                return success(response, "Top 5 mejor valorados");
        }

        @GetMapping("/buscar/duracion-maxima/{minutos}")
        public ResponseEntity<ApiResponse<List<CortometrajeResponseDto>>> buscarPorDuracionMaxima(
                        @PathVariable Integer minutos) {

                List<CortometrajeResponseDto> response = cortometrajeService.findByDuracionMenorOIgual(minutos);
                return success(response, "Cortometrajes con duración <= " + minutos + " minutos");
        }

        @PreAuthorize("hasAuthority('ROLE_USER')")
        @PostMapping
        public ResponseEntity<ApiResponse<CortometrajeResponseDto>> create(
                        @RequestBody @Valid CortometrajeRequestDto dto) {

                CortometrajeResponseDto response = cortometrajeService
                                .createWithValidation(dto, getUsername());

                return created(response, "Cortometraje creado correctamente");
        }

        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<CortometrajeResponseDto>> update(
                        @PathVariable Long id,
                        @RequestBody @Valid CortometrajeRequestDto dto) {

                CortometrajeResponseDto response = cortometrajeService
                                .updateIfOwnerOrAdmin(id, dto, getUsername());

                return success(response, "Cortometraje actualizado correctamente");
        }

        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
                cortometrajeService.deleteIfOwnerOrAdmin(id, getUsername());
                return success(null, "Cortometraje eliminado correctamente");
        }

}
