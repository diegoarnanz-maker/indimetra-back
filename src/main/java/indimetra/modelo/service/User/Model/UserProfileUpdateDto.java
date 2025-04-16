package indimetra.modelo.service.User.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para actualizar el perfil del usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para actualizar el perfil del usuario")
public class UserProfileUpdateDto {

    @Schema(description = "URL de la imagen de perfil del usuario", example = "https://misitio.com/perfil.jpg")
    private String profileImage;

    @Schema(description = "Enlaces a redes sociales del usuario", example = "https://twitter.com/usuario")
    private String socialLinks;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede superar los 50 caracteres")
    @Schema(description = "País de residencia del usuario", example = "España", required = true)
    private String country;
}
