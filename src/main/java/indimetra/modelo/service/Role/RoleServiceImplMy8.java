package indimetra.modelo.service.Role;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimetra.exception.BadRequestException;
import indimetra.exception.NotFoundException;
import indimetra.modelo.entity.Role;
import indimetra.modelo.repository.IRoleRepository;
import indimetra.modelo.service.Base.GenericDtoServiceImpl;
import indimetra.modelo.service.Role.Model.RoleRequestDto;
import indimetra.modelo.service.Role.Model.RoleResponseDto;
import indimetra.modelo.service.Role.Model.RoleUpdateRequestDto;

@Service
public class RoleServiceImplMy8 extends GenericDtoServiceImpl<Role, RoleRequestDto, RoleResponseDto, Long>
        implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    protected IRoleRepository getRepository() {
        return roleRepository;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    protected Class<RoleRequestDto> getRequestDtoClass() {
        return RoleRequestDto.class;
    }

    @Override
    protected Class<RoleResponseDto> getResponseDtoClass() {
        return RoleResponseDto.class;
    }

    @Override
    protected void setEntityId(Role entity, Long id) {
        entity.setId(id);
    }

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    @Override
    public Optional<Role> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("El nombre del rol no puede estar vacío");
        }
        return roleRepository.findByName(Role.RoleType.valueOf(name));
    }

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================

    @Override
    public RoleResponseDto updateDescription(Long id, RoleUpdateRequestDto dto) {
        Role role = read(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        role.setDescription(dto.getDescription());

        Role updated = getRepository().save(role);
        return modelMapper.map(updated, getResponseDtoClass());
    }

}
