package indimetra.modelo.service.Cortometraje.Model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO de respuesta para representar un cortometraje con todos sus datos
 * públicos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de salida con los datos del cortometraje.")
public class CortometrajeResponseDto {

    @Schema(description = "ID único del cortometraje", example = "1")
    private Long id;

    @Schema(description = "Título del cortometraje", example = "La historia de un pez volador")
    private String title;

    @Schema(description = "Descripción del cortometraje", example = "Un corto animado sobre un pez que sueña con volar.")
    private String description;

    @Schema(description = "Técnica utilizada", example = "Stop Motion")
    private String technique;

    @Schema(description = "Año de lanzamiento", example = "2023")
    private int releaseYear;

    @Schema(description = "Duración en minutos", example = "12")
    private int duration;

    @Schema(description = "Idioma del cortometraje", example = "Español")
    private String language;

    @Schema(description = "Valoración promedio del cortometraje", example = "4.3")
    private BigDecimal rating;

    @Schema(description = "URL de la imagen de portada", example = "https://indimetra.com/images/corto123.jpg")
    private String imageUrl;

    @Schema(description = "URL del video", example = "https://indimetra.com/videos/corto123.mp4")
    private String videoUrl;

    @Schema(description = "Nombre de la categoría", example = "Animación")
    private String category;

    @Schema(description = "Nombre del autor del cortometraje", example = "diegocreator")
    private String author;
}
