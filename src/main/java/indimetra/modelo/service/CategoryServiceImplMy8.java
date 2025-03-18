package indimetra.modelo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Category;
import indimetra.modelo.repository.ICategoryRepository;

@Service
public class CategoryServiceImplMy8 extends GenericoCRUDServiceImplMy8<Category, Long> implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    protected ICategoryRepository getRepository() {
        return categoryRepository;
    }
}
