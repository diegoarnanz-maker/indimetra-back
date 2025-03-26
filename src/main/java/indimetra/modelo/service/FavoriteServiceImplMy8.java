package indimetra.modelo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Favorite;
import indimetra.modelo.repository.IFavoriteRepository;

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
