package indimetra.restcontroller.base;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import indimetra.utils.ApiResponse;

/**
 * Clase base para todos los controladores REST del sistema.
 * Proporciona métodos utilitarios para simplificar la gestión
 * de respuestas estándar y la obtención del usuario autenticado.
 */
public abstract class BaseRestcontroller {

    /**
     * Obtiene el nombre de usuario del usuario autenticado actual.
     *
     * @return el nombre de usuario, o "Anónimo" si no hay autenticación activa.
     */
    protected String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "Anónimo";
    }

    /**
     * Verifica si el usuario autenticado tiene el rol de administrador.
     *
     * @return true si el usuario tiene el rol ROLE_ADMIN, false en caso contrario.
     */
    protected boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Devuelve una respuesta HTTP 200 (OK) con un cuerpo estándar de tipo
     * ApiResponse.
     *
     * @param <T>     Tipo de dato devuelto.
     * @param data    Datos a incluir en la respuesta.
     * @param message Mensaje descriptivo.
     * @return ResponseEntity con código 200 y cuerpo formateado.
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(data, message));
    }

    /**
     * Devuelve una respuesta HTTP 201 (Created) con un cuerpo estándar de tipo
     * ApiResponse.
     *
     * @param <T>     Tipo de dato devuelto.
     * @param data    Datos a incluir en la respuesta.
     * @param message Mensaje descriptivo.
     * @return ResponseEntity con código 201 y cuerpo formateado.
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(201).body(new ApiResponse<>(data, message));
    }

    /**
     * Devuelve una respuesta HTTP 400 (Bad Request) con un mensaje de error.
     *
     * @param <T>     Tipo de dato genérico (vacío en este caso).
     * @param message Mensaje de error.
     * @return ResponseEntity con código 400 y cuerpo formateado.
     */
    protected <T> ResponseEntity<ApiResponse<T>> failure(String message) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(message));
    }
}
