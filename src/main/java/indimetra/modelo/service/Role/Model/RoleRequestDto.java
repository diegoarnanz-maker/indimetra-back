package indimetra.modelo.service.Role.Model;

import indimetra.modelo.entity.Role;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO de entrada para la creación de un rol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de entrada para la creación de un rol")
public class RoleRequestDto {

    @NotNull(message = "El nombre del rol es obligatorio")
    @Schema(description = "Nombre del rol (ej: ROLE_ADMIN, ROLE_USER)", example = "ROLE_ADMIN", required = true)
    private Role.RoleType name;

    @Schema(description = "Descripción del rol", example = "Rol con privilegios de administrador")
    private String description;
}
