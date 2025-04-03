package indimetra.modelo.service.Favorite;

import java.util.List;

import indimetra.modelo.entity.Favorite;
import indimetra.modelo.service.Base.IGenericoCRUD;

public interface IFavoriteService extends IGenericoCRUD<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByUsername(String username);

    // void deleteByUserIdAndSeriesId(Long userId, Long seriesId);

    boolean isFavoriteOwner(Long userId, Long cortometrajeId);

}
