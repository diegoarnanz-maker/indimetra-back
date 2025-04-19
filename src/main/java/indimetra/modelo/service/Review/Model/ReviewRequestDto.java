package indimetra.modelo.service.Review.Model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO de entrada para crear o actualizar una reseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de entrada para crear o actualizar una reseña")
public class ReviewRequestDto {

    @Schema(description = "Puntuación del cortometraje", example = "4.5", minimum = "0.0", maximum = "5.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación mínima es 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "La calificación máxima es 5.0")
    private BigDecimal rating;

    @Schema(description = "Comentario sobre el cortometraje", example = "Excelente producción, gran animación", maxLength = 1000)
    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
    private String comment;

    @Schema(description = "ID del cortometraje al que pertenece la reseña", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del cortometraje es obligatorio")
    private Long cortometrajeId;
}
