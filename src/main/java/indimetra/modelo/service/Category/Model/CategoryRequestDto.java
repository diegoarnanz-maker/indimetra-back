package indimetra.modelo.service.Category.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para la creación o actualización de una categoría.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Petición para crear o actualizar una categoría.")
public class CategoryRequestDto {

    @Schema(description = "Nombre único de la categoría", example = "Animación", required = true, maxLength = 50)
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String name;

    @Schema(description = "Descripción opcional de la categoría", example = "Categoría que incluye cortometrajes realizados mediante animación.", maxLength = 500)
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;
}
