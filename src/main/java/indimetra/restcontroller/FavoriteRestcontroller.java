package indimetra.restcontroller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import indimetra.exception.BadRequestException;
import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Favorite;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Cortometraje.ICortometrajeService;
import indimetra.modelo.service.Favorite.IFavoriteService;
import indimetra.modelo.service.Favorite.Model.FavoriteRequestDto;
import indimetra.modelo.service.Favorite.Model.FavoriteResponseDto;
import indimetra.modelo.service.User.IUserService;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/favorite")
public class FavoriteRestcontroller extends BaseRestcontroller {

        @Autowired
        private IFavoriteService favoriteService;

        @Autowired
        private IUserService userService;

        @Autowired
        private ICortometrajeService cortometrajeService;

        @Autowired
        private ModelMapper modelMapper;

        // Hay que ver si al solo tener que enviar en el body el cortometrajeId,
        // enviarlo por el path
        @PostMapping
        public ResponseEntity<ApiResponse<FavoriteResponseDto>> addFavorite(
                        @RequestBody @Valid FavoriteRequestDto dto,
                        Authentication authentication) {

                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                Cortometraje cortometraje = cortometrajeService.read(dto.getCortometrajeId())
                                .orElseThrow(() -> new NotFoundException("Cortometraje no encontrado"));

                if (favoriteService.isFavoriteOwner(user.getId(), cortometraje.getId())) {
                        throw new BadRequestException("Este cortometraje ya está en tus favoritos");
                }

                Favorite favorite = Favorite.builder()
                                .user(user)
                                .cortometraje(cortometraje)
                                .build();

                Favorite saved = favoriteService.create(favorite);

                FavoriteResponseDto response = modelMapper.map(saved, FavoriteResponseDto.class);
                response.setUsername(user.getUsername());
                response.setCortometrajeId(cortometraje.getId());
                response.setCortometrajeTitle(cortometraje.getTitle());

                return created(response, "Favorito añadido correctamente");
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<FavoriteResponseDto>>> getMyFavorites(Authentication authentication) {
                String username = authentication.getName();

                List<FavoriteResponseDto> response = favoriteService.findByUsername(username).stream()
                                .map(fav -> {
                                        FavoriteResponseDto dto = modelMapper.map(fav, FavoriteResponseDto.class);
                                        dto.setUsername(fav.getUser().getUsername());
                                        dto.setCortometrajeId(fav.getCortometraje().getId());
                                        dto.setCortometrajeTitle(fav.getCortometraje().getTitle());
                                        return dto;
                                }).toList();

                return success(response, "Favoritos del usuario");
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteFavorite(@PathVariable Long id, Authentication authentication) {
                Favorite favorite = favoriteService.read(id)
                                .orElseThrow(() -> new NotFoundException("Favorito no encontrado"));

                User user = userService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                boolean isAdmin = user.getRoles().stream()
                                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));

                boolean isOwner = favorite.getUser().getId().equals(user.getId());

                if (!isAdmin && !isOwner) {
                        throw new ForbiddenException("No tienes permiso para eliminar este favorito");
                }

                favoriteService.delete(id);
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
