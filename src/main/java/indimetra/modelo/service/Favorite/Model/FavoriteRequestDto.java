package indimetra.modelo.service.Favorite.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO de petición para agregar o restaurar un cortometraje a los favoritos del
 * usuario autenticado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de entrada para marcar un cortometraje como favorito.")
public class FavoriteRequestDto {

    @NotNull(message = "El ID del cortometraje es obligatorio")
    @Schema(description = "ID del cortometraje que se desea añadir a favoritos", example = "42", required = true)
    private Long cortometrajeId;
}
