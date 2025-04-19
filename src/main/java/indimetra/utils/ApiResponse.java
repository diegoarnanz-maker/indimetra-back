package indimetra.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para respuestas API uniformes.
 *
 * @param <T> Tipo de dato contenido en la respuesta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura genérica de respuesta para todas las APIs")
public class ApiResponse<T> {

    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo sobre el resultado", example = "Operación realizada correctamente")
    private String message;

    @Schema(description = "Datos devueltos por la operación, si los hay")
    private T data;

    /**
     * Constructor para respuestas exitosas con datos y mensaje por defecto.
     * 
     * @param data Datos devueltos
     */
    public ApiResponse(T data) {
        this.success = true;
        this.message = "Operación exitosa";
        this.data = data;
    }

    /**
     * Constructor para respuestas exitosas con datos y mensaje personalizado.
     * 
     * @param data    Datos devueltos
     * @param message Mensaje personalizado
     */
    public ApiResponse(T data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    /**
     * Constructor para respuestas con error.
     * 
     * @param message Mensaje del error
     */
    public ApiResponse(String message) {
        this.success = false;
        this.message = message;
        this.data = null;
    }
}
