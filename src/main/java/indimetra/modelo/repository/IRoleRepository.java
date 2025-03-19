package indimetra.modelo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.Role.RoleType;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType  name);
}
