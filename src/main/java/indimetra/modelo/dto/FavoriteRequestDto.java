package indimetra.modelo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteRequestDto {
    @NotNull(message = "El ID del cortometraje es obligatorio")
    private Long cortometrajeId;
}
