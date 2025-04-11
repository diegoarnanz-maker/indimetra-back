package indimetra.modelo.service.Favorite;

import java.util.List;

import indimetra.modelo.entity.Favorite;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Favorite.Model.FavoriteRequestDto;
import indimetra.modelo.service.Favorite.Model.FavoriteResponseDto;

public interface IFavoriteService extends IGenericDtoService<Favorite, FavoriteRequestDto, FavoriteResponseDto, Long> {

    List<Favorite> findByUserId(Long userId);

    List<Favorite> findByUsername(String username);

    boolean isFavoriteOwner(Long userId, Long cortometrajeId);

    FavoriteResponseDto addFavorite(FavoriteRequestDto dto, String username);

    List<FavoriteResponseDto> findAllByUsername(String username);

    void deleteFavoriteIfOwnerOrAdmin(Long id, String username);
}
