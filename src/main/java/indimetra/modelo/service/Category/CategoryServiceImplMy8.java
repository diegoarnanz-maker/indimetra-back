package indimetra.modelo.service.Category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.modelo.entity.Category;
import indimetra.modelo.repository.ICategoryRepository;
import indimetra.modelo.service.Base.GenericDtoServiceImpl;
import indimetra.modelo.service.Category.Model.CategoryRequestDto;
import indimetra.modelo.service.Category.Model.CategoryResponseDto;

@Service
public class CategoryServiceImplMy8 extends
        GenericDtoServiceImpl<Category, CategoryRequestDto, CategoryResponseDto, Long> implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    protected ICategoryRepository getRepository() {
        return categoryRepository;
    }

    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    protected Class<CategoryRequestDto> getRequestDtoClass() {
        return CategoryRequestDto.class;
    }

    @Override
    protected Class<CategoryResponseDto> getResponseDtoClass() {
        return CategoryResponseDto.class;
    }

    // Usa en la lógica de actualización (update) para asignar manualmente el ID al entity, ya que los DTOs no lo tienen
    @Override
    protected void setEntityId(Category entity, Long id) {
        entity.setId(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("El nombre de la categoría no puede estar vacío");
        }
        return categoryRepository.findByName(name);
    }
}
