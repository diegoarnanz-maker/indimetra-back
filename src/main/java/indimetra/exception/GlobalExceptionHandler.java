package indimetra.exception;

import indimetra.exception.base.BaseApiException;
import indimetra.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura y devuelve respuestas estandarizadas en caso de errores.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Maneja errores cuando no se encuentra un usuario en el contexto de seguridad.
         *
         * @param ex excepción lanzada
         * @return respuesta con estado 404 y mensaje de error
         */
        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(ex.getMessage()));
        }

        /**
         * Maneja errores de validación provenientes de anotaciones
         * como @NotBlank, @Email, etc.
         *
         * @param ex excepción con errores de validación
         * @return respuesta con estado 400 y detalles de los campos erróneos
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
                        MethodArgumentNotValidException ex) {
                Map<String, String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .collect(Collectors.toMap(
                                                error -> error.getField(),
                                                error -> error.getDefaultMessage(),
                                                (msg1, msg2) -> msg1 // en caso de campos duplicados
                                ));

                ApiResponse<Map<String, String>> response = new ApiResponse<>();
                response.setSuccess(false);
                response.setMessage("Error de validación");
                response.setData(errors);

                return ResponseEntity.badRequest().body(response);
        }

        /**
         * Maneja todas las excepciones personalizadas que extienden BaseApiException.
         *
         * @param ex excepción personalizada
         * @return respuesta con el código de estado definido y mensaje de error
         */
        @ExceptionHandler(BaseApiException.class)
        public ResponseEntity<ApiResponse<Object>> handleBaseApiException(BaseApiException ex) {
                return ResponseEntity
                                .status(ex.getStatus())
                                .body(new ApiResponse<>(ex.getMessage()));
        }

        /**
         * Maneja cualquier excepción no controlada que no se haya capturado
         * explícitamente.
         *
         * @param ex excepción genérica
         * @return respuesta con estado 500 y mensaje de error genérico
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse<>("Error inesperado: " + ex.getMessage()));
        }
}
