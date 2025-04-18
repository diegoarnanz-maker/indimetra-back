package indimetra.modelo.service.Role.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO utilizado para actualizar la descripción de un rol existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO utilizado para actualizar la descripción de un rol existente")
public class RoleUpdateRequestDto {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Nueva descripción del rol", example = "Rol con permisos completos para administración")
    private String description;
}
