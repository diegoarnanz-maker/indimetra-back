package indimetra.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {

}
