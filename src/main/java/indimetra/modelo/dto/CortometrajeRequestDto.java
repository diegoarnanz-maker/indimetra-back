package indimetra.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CortometrajeRequestDto {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    private String technique;

    @Min(value = 1900, message = "El año de lanzamiento no puede ser menor a 1900")
    @Max(value = 2100, message = "El año de lanzamiento no puede ser mayor a 2030")
    private int releaseYear;

    @Min(value = 1, message = "La duración debe ser al menos de 1 minuto")
    private int duration;

    @NotBlank(message = "El idioma es obligatorio")
    private String language;

    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación debe ser entre 0 y 5")
    @DecimalMax(value = "5.0", inclusive = true, message = "La calificación debe ser entre 0 y 5")
    private BigDecimal rating;

    private String imageUrl;

    @NotBlank(message = "La URL del video es obligatoria")
    private String videoUrl;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;
}
