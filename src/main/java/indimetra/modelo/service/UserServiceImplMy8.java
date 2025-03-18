package indimetra.modelo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.User;
import indimetra.modelo.repository.IUserRepository;

@Service
public class UserServiceImplMy8 extends GenericoCRUDServiceImplMy8<User, Long> implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected IUserRepository getRepository() {
        return userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }
}
