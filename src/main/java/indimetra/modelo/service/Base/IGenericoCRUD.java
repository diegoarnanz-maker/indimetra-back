package indimetra.modelo.service.Base;

import java.util.List;
import java.util.Optional;

// Usamos javadoc para facilitar lectura pues es un interfaz de servicios genéricos

/**
 * Interfaz genérica para operaciones CRUD básicas.
 *
 * @param <E>  Tipo de entidad
 * @param <ID> Tipo del identificador de la entidad
 */
public interface IGenericoCRUD<E, ID> {

    /**
     * Obtiene todos los registros de la entidad.
     * @return Lista de todas las entidades
     */
    List<E> findAll();

    /**
     * Crea un nuevo registro en la base de datos.
     * @param entity La entidad a guardar
     * @return La entidad guardada
     */
    E create(E entity);

    /**
     * Obtiene un registro por su ID.
     * @param id El identificador de la entidad
     * @return Un `Optional<E>` que contiene la entidad si existe
     */
    Optional<E> read(ID id);

    /**
     * Actualiza un registro en la base de datos.
     * @param entity La entidad con los datos actualizados
     * @return La entidad actualizada
     */
    E update(E entity);

    /**
     * Elimina un registro de la base de datos por su ID.
     * @param id El identificador de la entidad a eliminar
     */
    void delete(ID id);
}
