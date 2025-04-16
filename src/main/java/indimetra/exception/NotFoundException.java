package indimetra.exception;

import org.springframework.http.HttpStatus;
import indimetra.exception.base.BaseApiException;

/**
 * Excepción personalizada para representar errores de recurso no encontrado
 * (404).
 * Se utiliza cuando un recurso solicitado no existe en el sistema.
 */
public class NotFoundException extends BaseApiException {

    /**
     * Crea una nueva instancia de NotFoundException con un mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error
     */
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
