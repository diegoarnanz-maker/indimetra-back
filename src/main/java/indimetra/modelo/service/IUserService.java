package indimetra.modelo.service;

import java.util.Optional;

import indimetra.modelo.entity.User;

public interface IUserService extends IGenericoCRUD<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    
}
