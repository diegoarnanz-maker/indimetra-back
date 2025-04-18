package indimetra.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Clase base para excepciones personalizadas en la API.
 * Permite asociar un {@link HttpStatus} específico a cada excepción.
 */
@Getter
public class BaseApiException extends RuntimeException {

    /**
     * Código de estado HTTP que representa esta excepción.
     */
    private final HttpStatus status;

    /**
     * Crea una nueva excepción personalizada con un mensaje y estado HTTP asociado.
     *
     * @param message Mensaje descriptivo del error
     * @param status  Código de estado HTTP a devolver
     */
    public BaseApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
