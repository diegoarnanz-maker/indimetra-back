package indimetra.modelo.service.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.Favorite;
import indimetra.modelo.entity.Review;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.ICortometrajeRepository;
import indimetra.modelo.repository.IFavoriteRepository;
import indimetra.modelo.repository.IReviewRepository;
import indimetra.modelo.repository.IRoleRepository;
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
    private IRoleRepository roleRepository;

    @Autowired
    private ICortometrajeRepository cortometrajeRepository;

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private IFavoriteRepository favoriteRepository;

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
        return userRepository.findByUsernameAndIsActiveTrueAndIsDeletedFalse(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("El correo electrónico no puede estar vacío");
        }
        return userRepository.findByEmailAndIsActiveTrueAndIsDeletedFalse(email);
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
            return userRepository.findByRoles_NameAndIsActiveTrueAndIsDeletedFalse(roleType).stream()
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

    @Override
    public void toggleRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        boolean isCurrentlyAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName() == Role.RoleType.ROLE_ADMIN);

        Role.RoleType newRoleType = isCurrentlyAdmin ? Role.RoleType.ROLE_USER : Role.RoleType.ROLE_ADMIN;

        Role newRole = roleRepository.findByName(newRoleType)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + newRoleType.name()));

        Set<Role> roles = new HashSet<>();
        roles.add(newRole);
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> findByUsernameContains(String username) {
        return userRepository.findByUsernameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(username).stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
    }

    @Override
    public Map<String, Integer> getUserCountByRole() {
        List<User> allUsers = userRepository.findAll();

        Map<String, Integer> roleCount = new HashMap<>();

        allUsers.forEach(user -> {
            user.getRoles().forEach(role -> {
                String roleName = role.getName().name().replace("ROLE_", "").toLowerCase();
                roleCount.put(roleName, roleCount.getOrDefault(roleName, 0) + 1);
            });
        });

        return roleCount;
    }

    @Override
    public void setUserActiveStatus(Long userId, boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        user.setIsActive(isActive);
        userRepository.save(user);

        // Desactivar/activar cortometrajes
        List<Cortometraje> cortos = cortometrajeRepository.findByUser(user);
        for (Cortometraje corto : cortos) {
            if (!corto.getIsDeleted()) {
                corto.setIsActive(isActive);
            }
        }
        cortometrajeRepository.saveAll(cortos);

        // Desactivar/activar reviews y favoritos según estado del corto
        for (Cortometraje corto : cortos) {
            if (!corto.getIsDeleted()) {

                List<Review> reviews = reviewRepository.findByCortometrajeId(corto.getId());
                for (Review r : reviews) {
                    r.setIsActive(isActive);
                }
                reviewRepository.saveAll(reviews);

                List<Favorite> favoritos = favoriteRepository.findByCortometrajeId(corto.getId());
                for (Favorite f : favoritos) {
                    f.setIsActive(isActive);
                }
                favoriteRepository.saveAll(favoritos);
            }
        }
    }

    @Override
    public void reactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        if (Boolean.TRUE.equals(user.getIsActive()) && Boolean.FALSE.equals(user.getIsDeleted())) {
            throw new BadRequestException("El usuario ya está activo.");
        }

        user.setIsActive(true);
        user.setIsDeleted(false);
        userRepository.save(user);

        // Reactivar también sus cortometrajes no eliminados
        List<Cortometraje> cortos = cortometrajeRepository.findByUser(user);
        for (Cortometraje corto : cortos) {
            if (!corto.getIsDeleted()) {
                corto.setIsActive(true);
            }
        }
        cortometrajeRepository.saveAll(cortos);

        // Reactivar también las reviews y favoritos si el cortometraje sigue activo
        for (Cortometraje corto : cortos) {
            if (!corto.getIsDeleted()) {

                // Reviews
                List<Review> reviews = reviewRepository.findByCortometrajeId(corto.getId());
                for (Review r : reviews) {
                    r.setIsActive(true);
                    r.setIsDeleted(false);
                }
                reviewRepository.saveAll(reviews);

                // Favoritos
                List<Favorite> favoritos = favoriteRepository.findByCortometrajeId(corto.getId());
                for (Favorite f : favoritos) {
                    f.setIsActive(true);
                    f.setIsDeleted(false);
                }
                favoriteRepository.saveAll(favoritos);
            }
        }
    }

    @Override
    public PagedResponse<UserResponseDto> findActiveUsersPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByIsActiveTrueAndIsDeletedFalse(pageable);

        List<UserResponseDto> data = userPage.getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();

        return PagedResponse.<UserResponseDto>builder()
                .message("Usuarios activos paginados correctamente")
                .data(data)
                .totalItems((int) userPage.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

    @Override
    public void softDeleteUser(Long id, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        if (user.getUsername().equals(currentUsername)) {
            throw new BadRequestException("No puedes eliminar tu propia cuenta desde este endpoint.");
        }

        user.setIsDeleted(true);
        user.setIsActive(false);
        userRepository.save(user);

        // Aplicar cascada
        applyCascadeSoftDelete(user);
    }

    @Override
    public void deleteMyAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (user.getIsDeleted()) {
            throw new BadRequestException("La cuenta ya ha sido eliminada");
        }

        user.setIsDeleted(true);
        user.setIsActive(false);
        userRepository.save(user);

        // Aplicar cascada
        applyCascadeSoftDelete(user);
    }

    // Método auxiliar para lógica en cascada
    private void applyCascadeSoftDelete(User user) {
        List<Cortometraje> cortos = cortometrajeRepository.findByUser(user);
        for (Cortometraje corto : cortos) {
            if (!corto.getIsDeleted()) {
                corto.setIsActive(false);
                corto.setIsDeleted(true);
            }
        }
        cortometrajeRepository.saveAll(cortos);

        for (Cortometraje corto : cortos) {
            // Reviews
            List<Review> reviews = reviewRepository.findByCortometrajeId(corto.getId());
            for (Review r : reviews) {
                r.setIsActive(false);
                r.setIsDeleted(true);
            }
            reviewRepository.saveAll(reviews);

            // Favoritos
            List<Favorite> favoritos = favoriteRepository.findByCortometrajeId(corto.getId());
            for (Favorite f : favoritos) {
                f.setIsActive(false);
                f.setIsDeleted(true);
            }
            favoriteRepository.saveAll(favoritos);
        }
    }

}
