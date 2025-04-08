package indimetra.modelo.service.User;

import java.util.Optional;

import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericoCRUD;

public interface IUserService extends IGenericoCRUD<User, Long> {

    void updateAuthorStatus(Long userId, boolean isAuthor);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
