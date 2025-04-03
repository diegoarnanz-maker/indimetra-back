package indimetra.modelo.service.Review.Model;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación mínima es 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "La calificación máxima es 5.0")
    private BigDecimal rating;

    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
    private String comment;

    @NotNull(message = "El ID del cortometraje es obligatorio")
    private Long cortometrajeId;
}
