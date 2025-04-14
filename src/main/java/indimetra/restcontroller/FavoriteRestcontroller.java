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
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/favorite")
public class FavoriteRestcontroller extends BaseRestcontroller {

        @Autowired
        private IFavoriteService favoriteService;

        // Hay que ver si al solo tener que enviar en el body el cortometrajeId,
        // enviarlo por el path
        @PreAuthorize("hasAuthority('ROLE_USER')")
        @PostMapping
        public ResponseEntity<ApiResponse<FavoriteResponseDto>> addFavorite(
                        @RequestBody @Valid FavoriteRequestDto dto) {

                FavoriteResponseDto response = favoriteService.addFavorite(dto, getUsername());
                return created(response, "Favorito añadido correctamente");
        }

        @PreAuthorize("hasAuthority('ROLE_USER')")
        @GetMapping("/mis-favoritos")
        public ResponseEntity<ApiResponse<List<FavoriteResponseDto>>> getMyFavorites() {
                List<FavoriteResponseDto> response = favoriteService.findAllByUsername(getUsername());
                return success(response, "Favoritos del usuario");
        }

        @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteFavorite(@PathVariable Long id) {
                favoriteService.deleteFavoriteIfOwnerOrAdmin(id, getUsername());
                return success(null, "Favorito eliminado correctamente");
        }

        // Solo util si se quiere obtener todos los favoritos para hacer algun modulo de
        // estadistica
        // @GetMapping("/all")
        // public ResponseEntity<List<FavoriteResponseDto>>
        // getAllFavorites(Authentication authentication) {
        // User user = userService.findByUsername(authentication.getName())
        // .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // boolean isAdmin = user.getRoles().stream()
        // .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));

        // if (!isAdmin) {
        // return ResponseEntity.status(403).build();
        // }

        // List<Favorite> allFavorites = favoriteService.findAll();

        // List<FavoriteResponseDto> response = allFavorites.stream()
        // .map(fav -> {
        // FavoriteResponseDto dto = modelMapper.map(fav, FavoriteResponseDto.class);
        // dto.setUsername(fav.getUser().getUsername());
        // dto.setCortometrajeId(fav.getCortometraje().getId());
        // dto.setCortometrajeTitle(fav.getCortometraje().getTitle());
        // return dto;
        // })
        // .collect(Collectors.toList());

        // return ResponseEntity.ok(response);
        // }
}
