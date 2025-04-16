package indimetra.modelo.service.Cortometraje.Model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO que representa los datos requeridos para crear o actualizar un
 * cortometraje.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de entrada para creación o actualización de un cortometraje.")
public class CortometrajeRequestDto {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    @Schema(description = "Título del cortometraje", example = "La historia de un pez volador")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripción del cortometraje", example = "Un corto animado sobre un pez que sueña con volar.")
    private String description;

    @Schema(description = "Técnica utilizada en el cortometraje", example = "Stop Motion")
    private String technique;

    @Min(value = 1900, message = "El año de lanzamiento no puede ser menor a 1900")
    @Max(value = 2100, message = "El año de lanzamiento no puede ser mayor a 2030")
    @Schema(description = "Año de lanzamiento", example = "2023")
    private int releaseYear;

    @Min(value = 1, message = "La duración debe ser al menos de 1 minuto")
    @Schema(description = "Duración en minutos", example = "12")
    private int duration;

    @NotBlank(message = "El idioma es obligatorio")
    @Schema(description = "Idioma del cortometraje", example = "Español")
    private String language;

    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación debe ser entre 0 y 5")
    @DecimalMax(value = "5.0", inclusive = true, message = "La calificación debe ser entre 0 y 5")
    @Schema(description = "Rating inicial del cortometraje", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "URL de la imagen de portada", example = "https://miapp.com/images/corto123.jpg")
    private String imageUrl;

    @NotBlank(message = "La URL del video es obligatoria")
    @Schema(description = "URL del video del cortometraje", example = "https://miapp.com/videos/corto123.mp4")
    private String videoUrl;

    @NotBlank(message = "La categoría es obligatoria")
    @Schema(description = "Nombre de la categoría a la que pertenece el cortometraje", example = "Animación")
    private String category;
}
