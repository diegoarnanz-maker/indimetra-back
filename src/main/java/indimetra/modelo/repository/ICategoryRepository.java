package indimetra.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

}
