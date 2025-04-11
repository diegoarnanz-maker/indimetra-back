package indimetra.modelo.service.Role;

import java.util.Optional;

import indimetra.modelo.entity.Role;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Role.Model.RoleRequestDto;
import indimetra.modelo.service.Role.Model.RoleResponseDto;
import indimetra.modelo.service.Role.Model.RoleUpdateRequestDto;

public interface IRoleService extends IGenericDtoService<Role, RoleRequestDto, RoleResponseDto, Long> {

    Optional<Role> findByName(String name);

    RoleResponseDto updateDescription(Long id, RoleUpdateRequestDto dto);

}
