package indimetra.modelo.service.Auth;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.repository.IUserRepository;
import indimetra.modelo.repository.IRoleRepository;
import indimetra.modelo.service.Auth.Model.LoginRequestDto;
import indimetra.modelo.service.Auth.Model.LoginResponseDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return modelMapper.map(user, LoginResponseDto.class);
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new EntityExistsException("El nombre de usuario ya está en uso");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EntityExistsException("El correo ya está en uso");
        }

        Role userRole = roleRepository.findByName(Role.RoleType.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Rol ROLE_USER no encontrado"));

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .profileImage(userDto.getProfileImage())
                .country(userDto.getCountry())
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto getAuthenticatedUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + username));

        return modelMapper.map(user, UserResponseDto.class);
    }
}
