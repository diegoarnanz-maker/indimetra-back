package indimetra.modelo.service.Favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Favorite;
import indimetra.modelo.repository.IFavoriteRepository;
import indimetra.modelo.service.Base.GenericoCRUDServiceImplMy8;

@Service
public class FavoriteServiceImplMy8 extends GenericoCRUDServiceImplMy8<Favorite, Long> implements IFavoriteService {

    @Autowired
    private IFavoriteRepository favoriteRepository;

    @Override
    protected IFavoriteRepository getRepository() {
        return favoriteRepository;
    }

    @Override
    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public List<Favorite> findByUsername(String username) {
        return favoriteRepository.findByUserUsername(username);
    }

    @Override
    public boolean isFavoriteOwner(Long userId, Long cortometrajeId) {
        return favoriteRepository.findByUserIdAndCortometrajeId(userId, cortometrajeId).isPresent();
    }

}
