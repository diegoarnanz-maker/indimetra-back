package indimetra.modelo.service.Category.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO de respuesta que representa los datos públicos de una categoría.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta que contiene los datos de una categoría.")
public class CategoryResponseDto {

    @Schema(description = "Identificador único de la categoría", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoría", example = "Animación")
    private String name;

    @Schema(description = "Descripción de la categoría", example = "Categoría que agrupa cortometrajes animados.")
    private String description;
}
