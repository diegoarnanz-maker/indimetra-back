package indimetra.modelo.service.Review.Model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO de salida que representa una reseña realizada por un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de salida que representa una reseña realizada por un usuario")
public class ReviewResponseDto {

    @Schema(description = "ID de la reseña", example = "101")
    private Long id;

    @Schema(description = "Calificación otorgada al cortometraje", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "Comentario del usuario sobre el cortometraje", example = "Muy buena historia y dirección")
    private String comment;

    @Schema(description = "Nombre de usuario del autor de la reseña", example = "usuario123")
    private String username;

    @Schema(description = "ID del cortometraje reseñado", example = "55")
    private Long cortometrajeId;

    @Schema(description = "Título del cortometraje reseñado", example = "El Viaje de Luna")
    private String cortometrajeTitle;
}
