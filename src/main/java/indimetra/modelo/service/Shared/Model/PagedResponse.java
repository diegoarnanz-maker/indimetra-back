package indimetra.modelo.service.Shared.Model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Clase genérica utilizada para representar una respuesta paginada en la API.
 *
 * @param <T> Tipo de los datos contenidos en la página
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta genérica paginada que contiene datos, metadatos y mensaje")
public class PagedResponse<T> {

    @Schema(description = "Mensaje descriptivo de la operación", example = "Listado de elementos paginado correctamente")
    private String message;

    @Schema(description = "Lista de elementos de la página actual")
    private List<T> data;

    @Schema(description = "Número total de elementos disponibles", example = "125")
    private int totalItems;

    @Schema(description = "Número de página actual (comienza en 0)", example = "0")
    private int page;

    @Schema(description = "Tamaño de la página (cantidad de elementos por página)", example = "10")
    private int pageSize;
}
