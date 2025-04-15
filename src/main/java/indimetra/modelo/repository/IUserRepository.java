package indimetra.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Role.RoleType;
import indimetra.modelo.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

    // Búsqueda directa por campos únicos
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // Búsqueda filtrada por estado (activos y no eliminados)
    Optional<User> findByUsernameAndIsActiveTrueAndIsDeletedFalse(String username);

    Optional<User> findByEmailAndIsActiveTrueAndIsDeletedFalse(String email);

    List<User> findByUsernameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String username);

    List<User> findByRoles_NameAndIsActiveTrueAndIsDeletedFalse(RoleType name);

    Page<User> findByIsActiveTrueAndIsDeletedFalse(Pageable pageable);

    List<User> findByIsActiveTrueAndIsDeletedFalse();
}
