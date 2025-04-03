package indimetra.modelo.service.Role;

import java.util.Optional;

import indimetra.modelo.entity.Role;
import indimetra.modelo.service.Base.IGenericoCRUD;

public interface IRoleService extends IGenericoCRUD<Role, Long> {

    Optional<Role> findByName(String name);

}
