package indimetra.modelo.service;

import java.util.Optional;

import indimetra.modelo.entity.Category;

public interface ICategoryService extends IGenericoCRUD<Category, Long> {

    Optional<Category> findByName(String name);
    
}
