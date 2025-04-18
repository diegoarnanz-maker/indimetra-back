package indimetra.modelo.service.Base;

import indimetra.exception.BadRequestException;
import indimetra.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementación base genérica para operaciones CRUD usando JpaRepository.
 *
 * @param <E>  Tipo de entidad
 * @param <ID> Tipo del identificador de la entidad
 */
public abstract class GenericoCRUDServiceImplMy8<E, ID> implements IGenericoCRUD<E, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericoCRUDServiceImplMy8.class);

    /**
     * Proporciona el repositorio JPA correspondiente a la entidad.
     *
     * @return JpaRepository de la entidad
     */
    protected abstract JpaRepository<E, ID> getRepository();

    /**
     * Recupera todos los registros de la entidad.
     *
     * @return Lista de todas las entidades
     */
    @Override
    public List<E> findAll() {
        try {
            return getRepository().findAll();
        } catch (Exception e) {
            LOGGER.error("Error al recuperar todos los registros: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al recuperar todos los registros");
        }
    }

    /**
     * Crea un nuevo registro de entidad en la base de datos.
     *
     * @param entity Entidad a crear
     * @return Entidad creada
     * @throws BadRequestException si la entidad es nula
     */
    @Override
    @Transactional
    public E create(E entity) {
        if (entity == null) {
            throw new BadRequestException("La entidad no puede ser nula");
        }

        try {
            LOGGER.info("Guardando entidad: {}", entity);
            return getRepository().save(entity);
        } catch (Exception e) {
            LOGGER.error("Error al crear la entidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al crear la entidad");
        }
    }

    /**
     * Recupera una entidad por su identificador.
     *
     * @param id Identificador de la entidad
     * @return Optional con la entidad si existe
     * @throws BadRequestException si el ID es nulo
     */
    @Override
    public Optional<E> read(ID id) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser nulo");
        }

        try {
            return getRepository().findById(id);
        } catch (Exception e) {
            LOGGER.error("Error al recuperar entidad por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al recuperar entidad por ID");
        }
    }

    /**
     * Actualiza una entidad existente.
     *
     * @param entity Entidad con datos actualizados
     * @return Entidad actualizada
     * @throws BadRequestException si la entidad es nula
     */
    @Override
    @Transactional
    public E update(E entity) {
        if (entity == null) {
            throw new BadRequestException("La entidad no puede ser nula");
        }

        try {
            LOGGER.info("Actualizando entidad: {}", entity);
            return getRepository().save(entity);
        } catch (Exception e) {
            LOGGER.error("Error al actualizar la entidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al actualizar la entidad");
        }
    }

    /**
     * Elimina una entidad por su identificador.
     *
     * @param id ID de la entidad a eliminar
     * @throws BadRequestException si el ID es nulo
     * @throws NotFoundException   si no existe una entidad con el ID dado
     */
    @Override
    @Transactional
    public void delete(ID id) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser nulo");
        }

        try {
            if (!getRepository().existsById(id)) {
                throw new NotFoundException("No se encontró la entidad con ID: " + id);
            }

            getRepository().deleteById(id);
            LOGGER.info("Entidad con ID {} eliminada exitosamente", id);

        } catch (Exception e) {
            LOGGER.error("Error al eliminar la entidad: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al eliminar la entidad");
        }
    }
}
