package indimetra.modelo.service.Category;

import java.util.Optional;

import indimetra.modelo.entity.Category;
import indimetra.modelo.service.Base.IGenericoCRUD;

public interface ICategoryService extends IGenericoCRUD<Category, Long> {

    Optional<Category> findByName(String name);
    
}
