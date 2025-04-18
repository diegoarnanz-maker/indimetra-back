package indimetra.modelo.service.Favorite;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.ForbiddenException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Favorite;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.IFavoriteRepository;
import indimetra.modelo.service.Base.GenericDtoServiceImpl;
import indimetra.modelo.service.Cortometraje.ICortometrajeService;
import indimetra.modelo.service.Favorite.Model.FavoriteRequestDto;
import indimetra.modelo.service.Favorite.Model.FavoriteResponseDto;
import indimetra.modelo.service.User.IUserService;

@Service
public class FavoriteServiceImplMy8
        extends GenericDtoServiceImpl<Favorite, FavoriteRequestDto, FavoriteResponseDto, Long>
        implements IFavoriteService {

    @Autowired
    private IFavoriteRepository favoriteRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICortometrajeService cortometrajeService;

    @Override
    protected IFavoriteRepository getRepository() {
        return favoriteRepository;
    }

    @Override
    protected Class<Favorite> getEntityClass() {
        return Favorite.class;
    }

    @Override
    protected Class<FavoriteRequestDto> getRequestDtoClass() {
        return FavoriteRequestDto.class;
    }

    @Override
    protected Class<FavoriteResponseDto> getResponseDtoClass() {
        return FavoriteResponseDto.class;
    }

    @Override
    protected void setEntityId(Favorite entity, Long id) {
        entity.setId(id);
    }

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    @Override
    public boolean isFavoriteOwner(Long userId, Long cortometrajeId) {
        return favoriteRepository.findByUserIdAndCortometrajeId(userId, cortometrajeId).isPresent();
    }

    @Override
    public List<Favorite> findByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BadRequestException("El ID del usuario no puede ser nulo o menor a 1");
        }
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public List<Favorite> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("El nombre de usuario no puede estar vacío");
        }
        return favoriteRepository.findByUserUsername(username);
    }

    @Override
    public List<FavoriteResponseDto> findAllByUsername(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        return favoriteRepository.findByUserId(user.getId()).stream()
                .filter(fav -> Boolean.TRUE.equals(fav.getIsActive()) &&
                        Boolean.FALSE.equals(fav.getIsDeleted()) &&
                        fav.getCortometraje() != null &&
                        Boolean.TRUE.equals(fav.getCortometraje().getIsActive()) &&
                        Boolean.FALSE.equals(fav.getCortometraje().getIsDeleted()) &&
                        fav.getCortometraje().getUser() != null &&
                        Boolean.TRUE.equals(fav.getCortometraje().getUser().getIsActive()))
                .map(fav -> FavoriteResponseDto.builder()
                        .id(fav.getId())
                        .username(fav.getUser().getUsername())
                        .cortometrajeId(fav.getCortometraje().getId())
                        .cortometrajeTitle(fav.getCortometraje().getTitle())
                        .createdAt(fav.getCreatedAt())
                        .build())
                .toList();
    }

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================

    @Override
    public FavoriteResponseDto addOrRestoreFavorite(FavoriteRequestDto dto, String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Cortometraje cortometraje = cortometrajeService.read(dto.getCortometrajeId())
                .orElseThrow(() -> new NotFoundException("Cortometraje no encontrado"));

        Optional<Favorite> existingFavoriteOpt = favoriteRepository
                .findByUserIdAndCortometrajeId(user.getId(), cortometraje.getId());

        Favorite favorite;

        if (existingFavoriteOpt.isPresent()) {
            favorite = existingFavoriteOpt.get();
            if (!favorite.getIsDeleted()) {
                throw new BadRequestException("Este cortometraje ya está en tus favoritos");
            }

            // Restaurar favorito
            favorite.setIsDeleted(false);
            favorite.setIsActive(true);
        } else {
            // Crear nuevo favorito
            favorite = new Favorite();
            favorite.setUser(user);
            favorite.setCortometraje(cortometraje);
            favorite.setIsDeleted(false);
            favorite.setIsActive(true);
        }

        Favorite saved = favoriteRepository.save(favorite);

        return FavoriteResponseDto.builder()
                .id(saved.getId())
                .username(user.getUsername())
                .cortometrajeId(cortometraje.getId())
                .cortometrajeTitle(cortometraje.getTitle())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // ============================================================
    // 🗑️ ELIMINACIÓN Y RESTAURACIÓN
    // ============================================================

    @Override
    public void deleteFavoriteIfOwnerOrAdmin(Long id, String username) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Favorito no encontrado"));

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));
        boolean isOwner = favorite.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("No tienes permiso para eliminar este favorito");
        }

        // Soft delete
        favorite.setIsDeleted(true);
        favorite.setIsActive(false);
        favoriteRepository.save(favorite);
    }
}
