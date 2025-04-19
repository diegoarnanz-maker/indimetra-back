package indimetra.exception;

import org.springframework.http.HttpStatus;
import indimetra.exception.base.BaseApiException;

/**
 * Excepción personalizada para representar errores de autorización (401).
 * Se lanza cuando un usuario no está autenticado o sus credenciales no son
 * válidas.
 */
public class UnauthorizedException extends BaseApiException {

    /**
     * Crea una nueva instancia de UnauthorizedException con un mensaje
     * personalizado.
     *
     * @param message Mensaje descriptivo del error
     */
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
