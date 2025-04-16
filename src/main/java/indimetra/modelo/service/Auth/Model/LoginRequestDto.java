package indimetra.modelo.service.Auth.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para peticiones de inicio de sesión.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición de login con credenciales del usuario.")
public class LoginRequestDto {

    @Schema(description = "Nombre de usuario", example = "diego_user", required = true)
    private String username;

    @Schema(description = "Contraseña del usuario", example = "1234secure!", required = true)
    private String password;
}
