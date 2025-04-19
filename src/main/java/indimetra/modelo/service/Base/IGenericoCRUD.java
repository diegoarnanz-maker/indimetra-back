package indimetra.modelo.service.Base;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones CRUD básicas directamente con entidades.
 *
 * @param <E>  Tipo de entidad gestionada
 * @param <ID> Tipo del identificador primario
 */
public interface IGenericoCRUD<E, ID> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Obtiene todos los registros de la entidad.
     *
     * @return Lista de todas las entidades
     */
    List<E> findAll();

    /**
     * Obtiene un registro por su ID.
     *
     * @param id Identificador del registro
     * @return Optional con la entidad si existe
     */
    Optional<E> read(ID id);

    // ============================================================
    // ➕ CREACIÓN Y ACTUALIZACIÓN
    // ============================================================

    /**
     * Crea un nuevo registro.
     *
     * @param entity Entidad a guardar
     * @return Entidad creada
     */
    E create(E entity);

    /**
     * Actualiza un registro existente.
     *
     * @param entity Entidad con los datos actualizados
     * @return Entidad actualizada
     */
    E update(E entity);

    // ============================================================
    // 🗑️ ELIMINACIÓN
    // ============================================================

    /**
     * Elimina un registro por su ID.
     *
     * @param id Identificador de la entidad a eliminar
     */
    void delete(ID id);
}
