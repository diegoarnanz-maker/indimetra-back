package indimetra.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Role.RoleType;
import indimetra.modelo.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRoles_Name(RoleType name);


    // Filtrados por isActive e isDeleted
    Optional<User> findByUsernameAndIsActiveTrueAndIsDeletedFalse(String username);
    Optional<User> findByEmailAndIsActiveTrueAndIsDeletedFalse(String email);
    List<User> findByUsernameContainingIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String username);
    List<User> findByRoles_NameAndIsActiveTrueAndIsDeletedFalse(RoleType name);
    Page<User> findByIsActiveTrueAndIsDeletedFalse(Pageable pageable);
    

}
