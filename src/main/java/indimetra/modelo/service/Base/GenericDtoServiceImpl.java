package indimetra.modelo.service.Base;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.exception.NotFoundException;

public abstract class GenericDtoServiceImpl<
    TEntity, 
    TRequestDto, 
    TResponseDto, 
    ID> implements IGenericDtoService<TEntity, TRequestDto, TResponseDto, ID> {

    @Autowired
    protected ModelMapper modelMapper;

    protected abstract JpaRepository<TEntity, ID> getRepository();

    protected abstract Class<TEntity> getEntityClass();
    protected abstract Class<TRequestDto> getRequestDtoClass();
    protected abstract Class<TResponseDto> getResponseDtoClass();

    @Override
    public List<TResponseDto> findAll() {
        return getRepository().findAll().stream()
            .map(entity -> modelMapper.map(entity, getResponseDtoClass()))
            .toList();
    }

    @Override
    public TResponseDto findById(ID id) {
        TEntity entity = getRepository().findById(id)
            .orElseThrow(() -> new NotFoundException("Entidad no encontrada con ID: " + id));

        return modelMapper.map(entity, getResponseDtoClass());
    }

    @Override
    public TResponseDto create(TRequestDto dto) {
        TEntity entity = modelMapper.map(dto, getEntityClass());
        TEntity saved = getRepository().save(entity);
        return modelMapper.map(saved, getResponseDtoClass());
    }

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

    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
            throw new NotFoundException("Entidad no encontrada con ID: " + id);
        }

        getRepository().deleteById(id);
    }

    /**
     * Método que debes implementar en cada servicio concreto para asignar el ID al entity antes de actualizar.
     */
    protected abstract void setEntityId(TEntity entity, ID id);
}

