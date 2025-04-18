package indimetra.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Auth.IAuthService;
import indimetra.modelo.service.Auth.Model.LoginRequestDto;
import indimetra.modelo.service.Auth.Model.LoginResponseDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auth Controller", description = "Autenticación y registro de usuarios")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthRestcontroller extends BaseRestcontroller {

    @Autowired
    private IAuthService authService;

    // ============================================
    // 🔐 ZONA AUTENTICACIÓN
    // ============================================

    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginDto) {
        LoginResponseDto response = authService.authenticateUser(loginDto);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        response.getUsername(),
                        null,
                        response.getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        role))
                                .toList()));

        return success(response, "Usuario logueado correctamente");
    }

    @Operation(summary = "Cerrar sesión")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        SecurityContextHolder.clearContext();
        return success(null, "Logout exitoso");
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(@RequestBody @Valid UserRequestDto userDto) {
        UserResponseDto newUser = authService.registerUser(userDto);
        return created(newUser, "Usuario registrado correctamente");
    }

    // ============================================
    // 👤 ZONA AUTENTICADO (ROLE_USER o ROLE_ADMIN)
    // ============================================

    @Operation(summary = "Obtener datos del usuario autenticado")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getAuthenticatedUser() {
        UserResponseDto user = authService.getAuthenticatedUser(getUsername());
        return success(user, "Usuario autenticado");
    }
}
