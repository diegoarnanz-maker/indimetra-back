package indimetra.modelo.service.Role.Model;

import indimetra.modelo.entity.Role.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO de salida para representar un rol del sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de salida para representar un rol del sistema")
public class RoleResponseDto {

    @Schema(description = "ID del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol (ej: ROLE_ADMIN, ROLE_USER)", example = "ROLE_USER")
    private RoleType name;

    @Schema(description = "Descripción del rol", example = "Rol con permisos de usuario estándar")
    private String description;
}
