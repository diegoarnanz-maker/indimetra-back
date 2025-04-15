package indimetra.modelo.service.Category;

import java.util.Optional;

import indimetra.modelo.entity.Category;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Category.Model.CategoryRequestDto;
import indimetra.modelo.service.Category.Model.CategoryResponseDto;

/**
 * Servicio para la gestión de categorías.
 */
public interface ICategoryService extends IGenericDtoService<Category, CategoryRequestDto, CategoryResponseDto, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Busca una categoría por su nombre.
     *
     * @param name nombre de la categoría
     * @return Optional con la categoría encontrada o vacío si no existe
     */
    Optional<Category> findByName(String name);
}
