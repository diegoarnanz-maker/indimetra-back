package indimetra.modelo.service.User.Model;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para el cambio de contraseña de un usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para cambiar la contraseña de un usuario")
public class UserChangePasswordDto {

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Schema(description = "Nueva contraseña del usuario", example = "MiNuevaClaveSegura123")
    private String newPassword;

    @NotBlank(message = "Debe repetir la nueva contraseña")
    @Schema(description = "Confirmación de la nueva contraseña", example = "MiNuevaClaveSegura123")
    private String repeatPassword;

    /**
     * Verifica si la nueva contraseña coincide con la repetida.
     *
     * @return true si ambas contraseñas coinciden, false en caso contrario
     */
    public boolean isPasswordConfirmed() {
        return newPassword != null && newPassword.equals(repeatPassword);
    }
}
