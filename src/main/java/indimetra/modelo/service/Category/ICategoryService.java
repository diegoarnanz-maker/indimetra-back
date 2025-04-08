package indimetra.modelo.service.Category;

import java.util.Optional;

import indimetra.modelo.entity.Category;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Category.Model.CategoryRequestDto;
import indimetra.modelo.service.Category.Model.CategoryResponseDto;

public interface ICategoryService extends IGenericDtoService<Category, CategoryRequestDto, CategoryResponseDto, Long> {

    Optional<Category> findByName(String name);
}
