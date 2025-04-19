package indimetra.modelo.service.Base;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para servicios que utilizan DTOs de entrada y salida.
 *
 * @param <TEntity>      Entidad principal gestionada
 * @param <TRequestDto>  DTO de entrada (creación/actualización)
 * @param <TResponseDto> DTO de salida (respuesta al cliente)
 * @param <ID>           Tipo del identificador primario de la entidad
 */
public interface IGenericDtoService<TEntity, TRequestDto, TResponseDto, ID> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Obtiene todos los elementos disponibles.
     *
     * @return Lista de DTOs de respuesta
     */
    List<TResponseDto> findAll();

    /**
     * Busca un elemento por su ID.
     *
     * @param id Identificador del elemento
     * @return DTO de respuesta correspondiente
     */
    TResponseDto findById(ID id);

    /**
     * Lee la entidad original sin convertir a DTO.
     *
     * @param id Identificador del elemento
     * @return Optional con la entidad si existe
     */
    Optional<TEntity> read(ID id);

    // ============================================================
    // ➕ CREACIÓN Y ACTUALIZACIÓN
    // ============================================================

    /**
     * Crea un nuevo elemento a partir del DTO de entrada.
     *
     * @param dto DTO con los datos de creación
     * @return DTO de respuesta creado
     */
    TResponseDto create(TRequestDto dto);

    /**
     * Actualiza un elemento existente.
     *
     * @param id  Identificador del elemento a actualizar
     * @param dto DTO con los nuevos datos
     * @return DTO de respuesta actualizado
     */
    TResponseDto update(ID id, TRequestDto dto);

    // ============================================================
    // 🗑️ ELIMINACIÓN
    // ============================================================

    /**
     * Elimina un elemento por su ID.
     *
     * @param id Identificador del elemento a eliminar
     */
    void delete(ID id);
}
