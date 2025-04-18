package indimetra.modelo.service.Cortometraje;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import indimetra.modelo.entity.Cortometraje;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeRequestDto;
import indimetra.modelo.service.Cortometraje.Model.CortometrajeResponseDto;
import indimetra.modelo.service.Shared.Model.PagedResponse;

/**
 * Servicio para la gestión de cortometrajes.
 */
public interface ICortometrajeService
        extends IGenericDtoService<Cortometraje, CortometrajeRequestDto, CortometrajeResponseDto, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Devuelve un listado paginado de cortometrajes visibles.
     *
     * @param pageable objeto de paginación
     * @return página con los DTOs de cortometrajes
     */
    Page<CortometrajeResponseDto> findAll(Pageable pageable);

    /**
     * Devuelve un listado paginado personalizado con total de elementos y tamaño de
     * página.
     *
     * @param pageable objeto de paginación
     * @return respuesta paginada con DTOs de cortometrajes
     */
    PagedResponse<CortometrajeResponseDto> findAllPaginated(Pageable pageable);

    /**
     * Busca los cortometrajes creados por un usuario específico (usado para "mis
     * cortos").
     *
     * @param username nombre del usuario
     * @return lista de cortometrajes del usuario autenticado
     */
    List<CortometrajeResponseDto> findByUsername(String username);

    /**
     * Busca cortometrajes de un autor específico (usuario público).
     *
     * @param username nombre del autor
     * @return lista de cortometrajes del autor
     */
    List<CortometrajeResponseDto> findByAuthor(String username);

    /**
     * Busca cortometrajes por idioma.
     *
     * @param language idioma del cortometraje
     * @return lista de cortometrajes en ese idioma
     */
    List<CortometrajeResponseDto> findByLanguage(String language);

    /**
     * Busca cortometrajes por nombre de categoría.
     *
     * @param category nombre de la categoría
     * @return lista de cortometrajes en esa categoría
     */
    List<CortometrajeResponseDto> findByCategory(String category);

    /**
     * Busca cortometrajes que contengan el texto en el título (ignorando
     * mayúsculas).
     *
     * @param title texto a buscar en el título
     * @return lista de cortometrajes que coincidan
     */
    List<CortometrajeResponseDto> findByTitleContainingIgnoreCase(String title);

    /**
     * Devuelve los cortometrajes con un rating mayor o igual al valor dado.
     *
     * @param rating mínimo rating deseado
     * @return lista de cortometrajes filtrados por rating
     */
    List<CortometrajeResponseDto> findByRating(Double rating);

    /**
     * Devuelve los cortometrajes cuya duración sea menor o igual al valor dado.
     *
     * @param minutos duración máxima en minutos
     * @return lista de cortometrajes filtrados por duración
     */
    List<CortometrajeResponseDto> findByDuracionMenorOIgual(Integer minutos);

    /**
     * Devuelve el top 5 de cortometrajes mejor valorados.
     *
     * @return lista de los mejores cortometrajes según rating
     */
    List<CortometrajeResponseDto> findTopRated();

    /**
     * Devuelve el top 5 de cortometrajes más recientes (por fecha de creación).
     *
     * @return lista de los cortometrajes más nuevos
     */
    List<CortometrajeResponseDto> findLatestSeries();

    /**
     * Devuelve un cortometraje solo si pertenece al usuario o es administrador.
     *
     * @param id      ID del cortometraje
     * @param usuario usuario autenticado
     * @return optional con el cortometraje si tiene permisos
     */
    Optional<Cortometraje> findByIdIfOwnerOrAdmin(Long id, User usuario);

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================

    /**
     * Actualiza el rating promedio de un cortometraje tras una reseña.
     *
     * @param id     ID del cortometraje
     * @param rating nuevo rating promedio
     */
    void updateRating(Long id, BigDecimal rating);

    /**
     * Recalcula y actualiza el rating promedio del cortometraje, considerando solo
     * las reseñas activas y no eliminadas.
     *
     * @param cortometrajeId ID del cortometraje
     */
    void actualizarRatingCortometraje(Long cortometrajeId);

    /**
     * Crea un nuevo cortometraje con validación de duplicados y relación con
     * autor/categoría.
     *
     * @param dto      datos del cortometraje
     * @param username nombre del usuario creador
     * @return DTO del cortometraje creado
     */
    CortometrajeResponseDto createWithValidation(CortometrajeRequestDto dto, String username);

    /**
     * Actualiza un cortometraje si el usuario es propietario o administrador.
     *
     * @param id       ID del cortometraje
     * @param dto      nuevos datos a actualizar
     * @param username usuario autenticado
     * @return DTO actualizado
     */
    CortometrajeResponseDto updateIfOwnerOrAdmin(Long id, CortometrajeRequestDto dto, String username);

    // ============================================================
    // 🗑️ ELIMINACIÓN Y RESTAURACIÓN
    // ============================================================

    /**
     * Elimina lógicamente un cortometraje si el usuario es el propietario o tiene
     * rol administrador.
     * También marca sus reseñas y favoritos como eliminados.
     *
     * @param id       ID del cortometraje
     * @param username nombre del usuario autenticado
     */
    void deleteIfOwnerOrAdmin(Long id, String username);
}
