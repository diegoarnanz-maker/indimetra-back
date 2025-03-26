package indimetra.restcontroller;

import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.dto.LoginRequestDto;
import indimetra.modelo.dto.LoginResponseDto;
import indimetra.modelo.dto.UserRequestDto;
import indimetra.modelo.dto.UserResponseDto;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.IUserService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthRestcontroller {

    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDto loginDto) {
        User user = userService.authenticateUser(loginDto.getUsername(), loginDto.getPassword());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));

        LoginResponseDto response = LoginResponseDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok(Map.of(
                "message", "Usuario logueado correctamente",
                "user", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userDto) {
        User newUser = userService.registerUser(userDto);
        return ResponseEntity.status(201).body(modelMapper.map(newUser, UserResponseDto.class));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getAuthenticatedUser(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + authentication.getName()));

        return ResponseEntity.ok(modelMapper.map(user, UserResponseDto.class));
    }
}
