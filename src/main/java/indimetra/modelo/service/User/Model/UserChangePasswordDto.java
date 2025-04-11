package indimetra.modelo.service.User.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangePasswordDto {

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    private String newPassword;

    @NotBlank(message = "Debe repetir la nueva contraseña")
    private String repeatPassword;

    public boolean isPasswordConfirmed() {
        return newPassword != null && newPassword.equals(repeatPassword);
    }
}
