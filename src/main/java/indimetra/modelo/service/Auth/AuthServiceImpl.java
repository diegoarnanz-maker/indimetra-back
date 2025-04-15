package indimetra.modelo.service.Auth;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.ConflictException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.helpers.Validators;
import indimetra.modelo.repository.IUserRepository;
import indimetra.modelo.repository.IRoleRepository;
import indimetra.modelo.service.Auth.Model.LoginRequestDto;
import indimetra.modelo.service.Auth.Model.LoginResponseDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;

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
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (!user.getIsActive()) {
            throw new BadRequestException("Tu cuenta está deshabilitada. Contacta con soporte.");
        }

        if (user.getIsDeleted()) {
            throw new BadRequestException("Tu cuenta ha sido eliminada.");
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        return modelMapper.map(user, LoginResponseDto.class);
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userDto) {
        if (!Validators.isValidEmail(userDto.getEmail())) {
            throw new BadRequestException("El formato del correo no es válido");
        }

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new ConflictException("El nombre de usuario ya está en uso");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ConflictException("El correo ya está en uso");
        }

        Role userRole = roleRepository.findByName(Role.RoleType.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Rol ROLE_USER no encontrado"));

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
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));

        if (!user.getIsActive()) {
            throw new BadRequestException("Tu cuenta está deshabilitada.");
        }

        if (user.getIsDeleted()) {
            throw new BadRequestException("Tu cuenta ha sido eliminada.");
        }

        return modelMapper.map(user, UserResponseDto.class);
    }

}
