package indimetra.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Favorite.IFavoriteService;
import indimetra.modelo.service.Favorite.Model.FavoriteRequestDto;
import indimetra.modelo.service.Favorite.Model.FavoriteResponseDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Favorite Controller", description = "Gestión de favoritos de cortometrajes")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/favorite")
public class FavoriteRestcontroller extends BaseRestcontroller {

        @Autowired
        private IFavoriteService favoriteService;

        // ============================================
        // 👤 ZONA USUARIO AUTENTICADO (ROLE_USER)
        // ============================================

        @Operation(summary = "Añadir un cortometraje a favoritos")
        @PreAuthorize("hasAuthority('ROLE_USER')")
        @PostMapping
        public ResponseEntity<ApiResponse<FavoriteResponseDto>> addFavorite(
                        @RequestBody @Valid FavoriteRequestDto dto) {

                FavoriteResponseDto response = favoriteService.addOrRestoreFavorite(dto, getUsername());
                return created(response, "Favorito añadido correctamente");
        }

        @Operation(summary = "Obtener los favoritos del usuario autenticado")
        @PreAuthorize("hasAuthority('ROLE_USER')")
        @GetMapping("/mis-favoritos")
        public ResponseEntity<ApiResponse<List<FavoriteResponseDto>>> getMyFavorites() {
                List<FavoriteResponseDto> response = favoriteService.findAllByUsername(getUsername());
                return success(response, "Favoritos del usuario");
        }

        // ============================================
        // 🔐 ZONA COMPARTIDA (ROLE_USER o ROLE_ADMIN)
        // ============================================

        @Operation(summary = "Eliminar un favorito (dueño o admin)")
        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteFavorite(@PathVariable Long id) {
                favoriteService.deleteFavoriteIfOwnerOrAdmin(id, getUsername());
                return success(null, "Favorito eliminado correctamente");
        }

        // ============================================
        // 🧪 OPCIONAL - Para uso interno o futuro (ADMIN)
        // ============================================

        // Este endpoint puede activarse en caso de estadísticas globales, uso interno,
        // etc.
        // @Operation(summary = "Obtener todos los favoritos (solo si se habilita para
        // estadísticas)")
        // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        // @GetMapping("/all")
        // public ResponseEntity<ApiResponse<List<FavoriteResponseDto>>>
        // getAllFavorites() {
        // List<FavoriteResponseDto> response = favoriteService.findAll(); // Asumiendo
        // que devuelve DTOs
        // return success(response, "Todos los favoritos (modo administrador)");
        // }
}
