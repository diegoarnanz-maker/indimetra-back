package indimetra.modelo.service.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.IUserRepository;
import indimetra.modelo.service.Base.GenericoCRUDServiceImplMy8;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImplMy8 extends GenericoCRUDServiceImplMy8<User, Long>
        implements IUserService, UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected IUserRepository getRepository() {
        return userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("El nombre de usuario no puede estar vacío");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("El correo electrónico no puede estar vacío");
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getAuthorities())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    @Override
    @Transactional
    public void updateAuthorStatus(Long userId, boolean isAuthor) {
        if (userId == null || userId <= 0) {
            throw new BadRequestException("ID de usuario inválido");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        existingUser.setIsAuthor(isAuthor);
        userRepository.save(existingUser);
    }
}
