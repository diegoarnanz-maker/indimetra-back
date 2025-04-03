package indimetra.modelo.service.User.Model;

import java.util.Set;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;
    private String country;
    private Set<String> roles;
}
