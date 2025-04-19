package indimetra.modelo.service.Role;

import java.util.Optional;

import indimetra.modelo.entity.Role;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Role.Model.RoleRequestDto;
import indimetra.modelo.service.Role.Model.RoleResponseDto;
import indimetra.modelo.service.Role.Model.RoleUpdateRequestDto;

public interface IRoleService extends IGenericDtoService<Role, RoleRequestDto, RoleResponseDto, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Busca un rol por su nombre.
     * 
     * @param name Nombre del rol (ej: "ROLE_ADMIN")
     * @return Optional con el rol encontrado o vacío si no existe.
     */
    Optional<Role> findByName(String name);

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================

    /**
     * Actualiza la descripción de un rol existente.
     * 
     * @param id  ID del rol a actualizar
     * @param dto Objeto con la nueva descripción
     * @return DTO actualizado del rol
     */
    RoleResponseDto updateDescription(Long id, RoleUpdateRequestDto dto);
}
