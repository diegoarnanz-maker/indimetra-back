package indimetra.modelo.service.User.Model;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para el registro o creación de un nuevo usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para registrar o crear un nuevo usuario")
public class UserRequestDto {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario único", example = "juanito99", required = true)
    private String username;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Correo electrónico válido del usuario", example = "correo@dominio.com", required = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "contrasena123", required = true)
    private String password;

    @Schema(description = "URL de la imagen de perfil", example = "https://indimetra.com/imagen.jpg")
    private String profileImage;

    @Schema(description = "País de residencia del usuario", example = "España")
    private String country;

    @Schema(description = "Lista de roles que se asignarán al usuario", example = "[\"ROLE_USER\"]")
    private Set<String> roles;

    @Schema(description = "Enlaces a redes sociales del usuario", example = "https://linkedin.com/in/usuario")
    private String socialLinks;
}
