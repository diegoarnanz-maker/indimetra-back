package indimetra.modelo.service;

import java.util.List;

import indimetra.modelo.entity.Favorite;

public interface IFavoriteService extends IGenericoCRUD<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByUsername(String username);

    // void deleteByUserIdAndSeriesId(Long userId, Long seriesId);

    boolean isFavoriteOwner(Long userId, Long cortometrajeId);

}
