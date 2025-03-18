package indimetra.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

}
