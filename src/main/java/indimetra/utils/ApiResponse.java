package indimetra.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // Constructor para éxito con datos, por defecto
    public ApiResponse(T data) {
        this.success = true;
        this.message = "Operación exitosa";
        this.data = data;
    }

    // Constructor para éxito con datos + mensaje personalizado
    public ApiResponse(T data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    // Constructor para fallo con mensaje personalizado
    public ApiResponse(String message) {
        this.success = false;
        this.message = message;
        this.data = null;
    }
}
