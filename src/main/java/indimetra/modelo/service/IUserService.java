package indimetra.modelo.service;

import java.util.Optional;

import indimetra.modelo.dto.UserRequestDto;
import indimetra.modelo.entity.User;

public interface IUserService extends IGenericoCRUD<User, Long> {

    User authenticateUser(String username, String password);

    User registerUser(UserRequestDto userDto);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
