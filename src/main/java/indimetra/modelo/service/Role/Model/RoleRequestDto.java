package indimetra.modelo.service.Role.Model;

import indimetra.modelo.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDto {

    @NotNull(message = "El nombre del rol es obligatorio")
    private Role.RoleType name;

    private String description;
}
