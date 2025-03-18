package indimetra.modelo.dto;

import java.util.Set;

// import indimetra.modelo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String username;
    private String email;
    private String password;
    private String profileImage;
    private String country;

    // private Set<Role> roles;
    private Set<String> roles;
    private String socialLinks;
}
