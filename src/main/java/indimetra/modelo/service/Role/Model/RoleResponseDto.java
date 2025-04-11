package indimetra.modelo.service.Role.Model;

import indimetra.modelo.entity.Role.RoleType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDto {

    private Long id;
    private RoleType name;
    private String description;
}
