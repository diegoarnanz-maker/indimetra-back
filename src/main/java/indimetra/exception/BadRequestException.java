package indimetra.exception;

import org.springframework.http.HttpStatus;
import indimetra.exception.base.BaseApiException;

/**
 * Excepción lanzada cuando una solicitud del cliente es inválida o contiene
 * datos incorrectos.
 * 
 * Representa un error 400 Bad Request.
 */
public class BadRequestException extends BaseApiException {

    /**
     * Crea una excepción de tipo BadRequest con un mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error
     */
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
