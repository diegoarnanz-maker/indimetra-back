package indimetra.modelo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import indimetra.modelo.entity.Category;

/**
 * Repositorio para la entidad {@link Category}.
 * <p>
 * Proporciona métodos CRUD y consultas personalizadas para categorías.
 */
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    // ============================================================
    // 🔍 BÚSQUEDAS PERSONALIZADAS
    // ============================================================

    /**
     * Busca una categoría por su nombre.
     *
     * @param name el nombre de la categoría
     * @return un {@link Optional} que contiene la categoría si existe, o vacío en
     *         caso contrario
     */
    Optional<Category> findByName(String name);
}
