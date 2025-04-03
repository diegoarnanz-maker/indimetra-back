package indimetra.modelo.service.User;

import java.util.Optional;

import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericoCRUD;
import indimetra.modelo.service.User.Model.UserRequestDto;

public interface IUserService extends IGenericoCRUD<User, Long> {

    User authenticateUser(String username, String password);

    User registerUser(UserRequestDto userDto);

    void updateAuthorStatus(Long userId, boolean isAuthor);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
