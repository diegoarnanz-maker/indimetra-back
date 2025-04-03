package indimetra.modelo.service.Category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Category;
import indimetra.modelo.repository.ICategoryRepository;
import indimetra.modelo.service.Base.GenericoCRUDServiceImplMy8;

@Service
public class CategoryServiceImplMy8 extends GenericoCRUDServiceImplMy8<Category, Long> implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    protected ICategoryRepository getRepository() {
        return categoryRepository;
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
