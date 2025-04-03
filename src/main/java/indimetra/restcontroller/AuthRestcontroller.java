package indimetra.restcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Auth.IAuthService;
import indimetra.modelo.service.Auth.Model.LoginRequestDto;
import indimetra.modelo.service.Auth.Model.LoginResponseDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthRestcontroller {

    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDto loginDto) {
        LoginResponseDto response = authService.authenticateUser(loginDto);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        response.getUsername(), null, response.getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        role))
                                .toList()));

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
        UserResponseDto newUser = authService.registerUser(userDto);
        return ResponseEntity.status(201).body(newUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getAuthenticatedUser(Authentication authentication) {
        UserResponseDto user = authService.getAuthenticatedUser(authentication.getName());
        return ResponseEntity.ok(user);
    }
}
