package indimetra.modelo.service;

import java.util.Optional;

import indimetra.modelo.entity.Role;

public interface IRoleService extends IGenericoCRUD<Role, Long> {

    Optional<Role> findByName(String name);

}
