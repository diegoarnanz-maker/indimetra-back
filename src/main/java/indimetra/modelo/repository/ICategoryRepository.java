package indimetra.modelo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    
}
