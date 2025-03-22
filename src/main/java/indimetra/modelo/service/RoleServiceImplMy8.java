package indimetra.modelo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.Role.RoleType;
import indimetra.modelo.repository.IRoleRepository;

@Service
public class RoleServiceImplMy8 extends GenericoCRUDServiceImplMy8<Role, Long> implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    protected IRoleRepository getRepository() {
        return roleRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            RoleType roleType = RoleType.valueOf(name);
            return roleRepository.findByName(roleType);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
