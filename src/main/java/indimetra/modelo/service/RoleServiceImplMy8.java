package indimetra.modelo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.modelo.entity.Role;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }
}
