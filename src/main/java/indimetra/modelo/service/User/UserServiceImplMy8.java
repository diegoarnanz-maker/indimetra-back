package indimetra.modelo.service.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.IUserRepository;
import indimetra.modelo.service.Base.GenericDtoServiceImpl;
import indimetra.modelo.service.Shared.Model.PagedResponse;
import indimetra.modelo.service.User.Model.UserChangePasswordDto;
import indimetra.modelo.service.User.Model.UserProfileUpdateDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;

@Service
public class UserServiceImplMy8
        extends GenericDtoServiceImpl<User, UserRequestDto, UserResponseDto, Long>
        implements IUserService, UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected IUserRepository getRepository() {
        return userRepository;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserRequestDto> getRequestDtoClass() {
        return UserRequestDto.class;
    }

    @Override
    protected Class<UserResponseDto> getResponseDtoClass() {
        return UserResponseDto.class;
    }

    @Override
    protected void setEntityId(User entity, Long id) {
        entity.setId(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("El nombre de usuario no puede estar vacío");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("El correo electrónico no puede estar vacío");
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateAuthorStatus(Long userId, boolean isAuthor) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));
        user.setIsAuthor(isAuthor);
        userRepository.save(user);
    }

    @Override
    public UserResponseDto updateProfile(String username, UserProfileUpdateDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con username: " + username));

        user.setProfileImage(dto.getProfileImage());
        user.setSocialLinks(dto.getSocialLinks());
        user.setCountry(dto.getCountry());

        User updated = userRepository.save(user);
        return modelMapper.map(updated, UserResponseDto.class);
    }

    @Override
    public void deleteIfNotAdmin(Long id, String username) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().name().equals("ROLE_ADMIN"));

        if (isAdmin) {
            throw new BadRequestException("No se puede eliminar a un administrador");
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
    }

    @Override
    public void changePassword(String username, UserChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getRepeatPassword())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    // @Override
    // public void toggleUserStatus(Long userId, boolean enabled) {
    // User user = userRepository.findById(userId)
    // .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " +
    // userId));

    // user.setEnabled(enabled);
    // userRepository.save(user);
    // }

    @Override
    public List<UserResponseDto> findByRole(String role) {
        try {
            Role.RoleType roleType = Role.RoleType.valueOf(role);
            return userRepository.findByRoles_Name(roleType).stream()
                    .map(user -> modelMapper.map(user, UserResponseDto.class))
                    .toList();
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Rol no válido: " + role);
        }
    }

    @Override
    public PagedResponse<UserResponseDto> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> data = userPage.getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();

        return PagedResponse.<UserResponseDto>builder()
                .message("Usuarios paginados correctamente")
                .data(data)
                .totalItems((int) userPage.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

}
