package indimetra.modelo.service.Base;

import java.util.List;

public interface IGenericDtoService<
    TEntity, 
    TRequestDto, 
    TResponseDto, 
    ID> {

    List<TResponseDto> findAll();

    TResponseDto findById(ID id);

    TResponseDto create(TRequestDto dto);

    TResponseDto update(ID id, TRequestDto dto);

    void delete(ID id);
}

