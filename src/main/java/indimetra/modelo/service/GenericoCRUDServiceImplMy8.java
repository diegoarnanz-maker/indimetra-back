package indimetra.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericoCRUDServiceImplMy8<E, ID> implements IGenericoCRUD<E, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericoCRUDServiceImplMy8.class);

    protected abstract JpaRepository<E, ID> getRepository();

    @Override
    public List<E> findAll() {
        try {
            return getRepository().findAll();
        } catch (Exception e) {
            LOGGER.error("Error al recuperar todos los registros: {}", e.getMessage(), e);
            throw new ServiceException("Error al recuperar todos los registros", e);
        }
    }

    @Override
    @Transactional
    public E create(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }

        try {
            LOGGER.info("Guardando entidad: {}", entity);
            return getRepository().save(entity);
        } catch (Exception e) {
            LOGGER.error("Error al crear la entidad: {}", e.getMessage(), e);
            throw new ServiceException("Error al crear la entidad", e);
        }
    }

    @Override
    public Optional<E> read(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            return getRepository().findById(id);
        } catch (Exception e) {
            LOGGER.error("Error al recuperar entidad por ID: {}", e.getMessage(), e);
            throw new ServiceException("Error al recuperar entidad por ID", e);
        }
    }

    @Override
    @Transactional
    public E update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }

        try {
            LOGGER.info("Actualizando entidad: {}", entity);
            return getRepository().save(entity);
        } catch (Exception e) {
            LOGGER.error("Error al actualizar la entidad: {}", e.getMessage(), e);
            throw new ServiceException("Error al actualizar la entidad", e);
        }
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            if (!getRepository().existsById(id)) {
                throw new EntityNotFoundException("No se encontró la entidad con ID: " + id);
            }

            getRepository().deleteById(id);
            LOGGER.info("Entidad con ID {} eliminada exitosamente", id);

        } catch (Exception e) {
            LOGGER.error("Error al eliminar la entidad: {}", e.getMessage(), e);
            throw new ServiceException("Error al eliminar la entidad", e);
        }
    }
}
