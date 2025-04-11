package indimetra.modelo.service.User.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateDto {

    private String profileImage;
    private String socialLinks;
    
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede superar los 50 caracteres")
    private String country;
}
