package indimetra.modelo.service.Role;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.Role.RoleType;
import indimetra.modelo.repository.IRoleRepository;
import indimetra.modelo.service.Base.GenericoCRUDServiceImplMy8;

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
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol no puede estar vacío");
        }

        try {
            RoleType roleType = RoleType.valueOf(name.toUpperCase());
            return roleRepository.findByName(roleType);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El nombre del rol no es válido: " + name);
        }
    }

    public Role findByNameOrThrow(String name) {
        return findByName(name)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + name));
    }
}
