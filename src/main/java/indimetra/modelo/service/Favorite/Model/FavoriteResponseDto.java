package indimetra.modelo.service.Favorite.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * DTO de salida que representa un cortometraje marcado como favorito por un
 * usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de salida con la información de un cortometraje favorito")
public class FavoriteResponseDto {

    @Schema(description = "ID del favorito", example = "1")
    private Long id;

    @Schema(description = "ID del cortometraje marcado como favorito", example = "42")
    private Long cortometrajeId;

    @Schema(description = "Título del cortometraje", example = "La noche estrellada")
    private String cortometrajeTitle;

    @Schema(description = "Nombre de usuario que marcó el cortometraje como favorito", example = "usuario123")
    private String username;

    @Schema(description = "Fecha en la que se marcó el cortometraje como favorito", example = "2024-04-15T10:30:00Z")
    private Date createdAt;
}
