package indimetra.modelo.service.Role.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleUpdateRequestDto {

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;
}
