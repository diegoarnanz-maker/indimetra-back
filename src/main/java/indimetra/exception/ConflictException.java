package indimetra.exception;

import org.springframework.http.HttpStatus;
import indimetra.exception.base.BaseApiException;

/**
 * Excepción lanzada cuando hay un conflicto en la solicitud, como una violación
 * de unicidad.
 *
 * Representa un error 409 Conflict.
 */
public class ConflictException extends BaseApiException {

    /**
     * Crea una excepción de tipo Conflict con un mensaje personalizado.
     *
     * @param message Mensaje descriptivo del conflicto
     */
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
