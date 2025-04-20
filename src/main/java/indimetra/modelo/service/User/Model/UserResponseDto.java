package indimetra.modelo.service.User.Model;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO de respuesta con la información pública y de estado de un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de salida con los datos del usuario")
public class UserResponseDto {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "juan99")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "correo@dominio.com")
    private String email;

    @Schema(description = "URL de la imagen de perfil del usuario", example = "https://indimetra.com/avatar.jpg")
    private String profileImage;
    
    @Schema(description = "Enlace del usuario a redes sociales", example = "https://twitter.com/usuario")
    private String socialLinks;

    @Schema(description = "País de residencia", example = "España")
    private String country;

    @Schema(description = "Lista de roles asignados al usuario", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<String> roles;

    @Schema(description = "Indica si el usuario es autor de cortometrajes", example = "true")
    private Boolean isAuthor;

    @Schema(description = "Indica si el usuario está activo", example = "true")
    private Boolean isActive;

    @Schema(description = "Indica si el usuario ha sido eliminado lógicamente", example = "false")
    private Boolean isDeleted;
}
