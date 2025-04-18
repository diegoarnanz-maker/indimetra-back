package indimetra.modelo.service.Base;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.exception.NotFoundException;

/**
 * Implementación base genérica para servicios que utilizan DTOs, ModelMapper
 * y JpaRepository.
 *
 * @param <TEntity>      Tipo de la entidad
 * @param <TRequestDto>  Tipo del DTO de entrada (request)
 * @param <TResponseDto> Tipo del DTO de salida (response)
 * @param <ID>           Tipo del identificador de la entidad
 */
public abstract class GenericDtoServiceImpl<TEntity, TRequestDto, TResponseDto, ID>
        implements IGenericDtoService<TEntity, TRequestDto, TResponseDto, ID> {

    @Autowired
    protected ModelMapper modelMapper;

    /**
     * Devuelve el repositorio correspondiente a la entidad.
     *
     * @return Repositorio JPA
     */
    protected abstract JpaRepository<TEntity, ID> getRepository();

    /**
     * Devuelve la clase de la entidad.
     *
     * @return Clase de la entidad
     */
    protected abstract Class<TEntity> getEntityClass();

    /**
     * Devuelve la clase del DTO de entrada.
     *
     * @return Clase del DTO request
     */
    protected abstract Class<TRequestDto> getRequestDtoClass();

    /**
     * Devuelve la clase del DTO de salida.
     *
     * @return Clase del DTO response
     */
    protected abstract Class<TResponseDto> getResponseDtoClass();

    /**
     * Obtiene todos los registros en formato DTO.
     *
     * @return Lista de DTOs de respuesta
     */
    @Override
    public List<TResponseDto> findAll() {
        return getRepository().findAll().stream()
                .map(entity -> modelMapper.map(entity, getResponseDtoClass()))
                .toList();
    }

    /**
     * Busca un registro por ID y lo devuelve como DTO.
     *
     * @param id ID de la entidad
     * @return DTO de respuesta
     * @throws NotFoundException si no se encuentra la entidad
     */
    @Override
    public TResponseDto findById(ID id) {
        TEntity entity = getRepository().findById(id)
                .orElseThrow(() -> new NotFoundException("Entidad no encontrada con ID: " + id));

        return modelMapper.map(entity, getResponseDtoClass());
    }

    /**
     * Crea un nuevo registro a partir de un DTO de entrada.
     *
     * @param dto DTO con los datos a guardar
     * @return DTO de respuesta con los datos guardados
     */
    @Override
    public TResponseDto create(TRequestDto dto) {
        TEntity entity = modelMapper.map(dto, getEntityClass());
        TEntity saved = getRepository().save(entity);
        return modelMapper.map(saved, getResponseDtoClass());
    }

    /**
     * Actualiza un registro existente a partir de un DTO.
     *
     * @param id  ID del registro a actualizar
     * @param dto DTO con los nuevos datos
     * @return DTO actualizado
     * @throws NotFoundException si no existe un registro con el ID proporcionado
     */
    @Override
    public TResponseDto update(ID id, TRequestDto dto) {
        if (!getRepository().existsById(id)) {
            throw new NotFoundException("Entidad no encontrada con ID: " + id);
        }

        TEntity entity = modelMapper.map(dto, getEntityClass());
        setEntityId(entity, id);
        TEntity updated = getRepository().save(entity);

        return modelMapper.map(updated, getResponseDtoClass());
    }

    /**
     * Elimina un registro por su ID.
     *
     * @param id ID del registro a eliminar
     * @throws NotFoundException si no existe un registro con ese ID
     */
    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
            throw new NotFoundException("Entidad no encontrada con ID: " + id);
        }

        getRepository().deleteById(id);
    }

    /**
     * Lee una entidad directamente (sin mapear a DTO).
     *
     * @param id ID de la entidad
     * @return Optional con la entidad
     */
    @Override
    public Optional<TEntity> read(ID id) {
        return getRepository().findById(id);
    }

    /**
     * Recupera una entidad por su ID o lanza excepción si no existe.
     *
     * @param id ID de la entidad
     * @return Entidad encontrada
     * @throws NotFoundException si no se encuentra la entidad
     */
    protected TEntity readEntityById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new NotFoundException("Entidad no encontrada con ID: " + id));
    }

    /**
     * Método abstracto para establecer manualmente el ID en la entidad
     * antes de realizar una actualización.
     *
     * @param entity Entidad a modificar
     * @param id     ID que se debe establecer
     */
    protected abstract void setEntityId(TEntity entity, ID id);
}
