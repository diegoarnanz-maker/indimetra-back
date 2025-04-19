package indimetra.exception;

import org.springframework.http.HttpStatus;
import indimetra.exception.base.BaseApiException;

/**
 * Excepción lanzada cuando un usuario intenta acceder a un recurso sin permisos
 * suficientes.
 *
 * Representa un error 403 Forbidden.
 */
public class ForbiddenException extends BaseApiException {

    /**
     * Crea una excepción de tipo Forbidden con un mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error de autorización
     */
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
