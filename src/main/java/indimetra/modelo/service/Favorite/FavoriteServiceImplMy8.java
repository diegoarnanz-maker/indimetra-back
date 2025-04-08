package indimetra.modelo.service.Favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
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
    public boolean isFavoriteOwner(Long userId, Long cortometrajeId) {
        if (userId == null || cortometrajeId == null) {
            throw new BadRequestException("El ID del usuario y del cortometraje no pueden ser nulos");
        }

        return favoriteRepository.findByUserIdAndCortometrajeId(userId, cortometrajeId).isPresent();
    }
}
