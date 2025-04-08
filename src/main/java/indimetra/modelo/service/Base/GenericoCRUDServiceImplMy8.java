package indimetra.modelo.service.Base;

import indimetra.exception.BadRequestException;
import indimetra.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public abstract class GenericoCRUDServiceImplMy8<E, ID> implements IGenericoCRUD<E, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericoCRUDServiceImplMy8.class);

    protected abstract JpaRepository<E, ID> getRepository();

    @Override
    public List<E> findAll() {
        try {
            return getRepository().findAll();
        } catch (Exception e) {
            LOGGER.error("Error al recuperar todos los registros: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al recuperar todos los registros");
        }
    }

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
