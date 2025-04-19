package indimetra.modelo.service.Auth.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

/**
 * DTO de respuesta tras un login exitoso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta tras un login exitoso. Contiene la información del usuario autenticado.")
public class LoginResponseDto {

    @Schema(description = "Nombre de usuario autenticado", example = "diego_user")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "diego@example.com")
    private String email;

    @Schema(description = "Roles asignados al usuario", example = "[\"ROLE_USER\"]")
    private Set<String> roles;

    @Schema(description = "URL o nombre del archivo de imagen de perfil", example = "perfil1.jpg")
    private String profileImage;
}
