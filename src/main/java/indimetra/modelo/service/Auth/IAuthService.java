package indimetra.modelo.service.Auth;

import indimetra.modelo.service.Auth.Model.LoginRequestDto;
import indimetra.modelo.service.Auth.Model.LoginResponseDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;

/**
 * Servicio de autenticación y registro de usuarios.
 * Gestiona el login, registro y recuperación del usuario autenticado.
 */
public interface IAuthService {

    // ============================================================
    // 🔐 AUTENTICACIÓN
    // ============================================================

    /**
     * Autentica a un usuario utilizando credenciales (username y password).
     *
     * @param loginDto DTO con los datos de inicio de sesión
     * @return respuesta con los datos del usuario autenticado y roles
     */
    LoginResponseDto authenticateUser(LoginRequestDto loginDto);

    // ============================================================
    // 📝 REGISTRO
    // ============================================================

    /**
     * Registra un nuevo usuario en el sistema, validando duplicados.
     *
     * @param userDto DTO con los datos del nuevo usuario
     * @return respuesta con los datos del usuario creado
     */
    UserResponseDto registerUser(UserRequestDto userDto);

    // ============================================================
    // 🙋 USUARIO AUTENTICADO
    // ============================================================

    /**
     * Obtiene los datos del usuario actualmente autenticado a partir del nombre de
     * usuario.
     *
     * @param username nombre del usuario autenticado
     * @return DTO con la información del usuario
     */
    UserResponseDto getAuthenticatedUser(String username);
}
