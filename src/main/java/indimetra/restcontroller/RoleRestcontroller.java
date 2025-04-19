package indimetra.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Role.IRoleService;
import indimetra.modelo.service.Role.Model.RoleResponseDto;
import indimetra.modelo.service.Role.Model.RoleUpdateRequestDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Role Controller", description = "Gestión de roles de usuario")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/role")
public class RoleRestcontroller extends BaseRestcontroller {

    @Autowired
    private IRoleService roleService;

    // ============================================
    // 🔐 ZONA ADMIN (ROLE_ADMIN)
    // ============================================

    // 🔹 LECTURA

    @Operation(summary = "Obtener todos los roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> findAll() {
        List<RoleResponseDto> response = roleService.findAll();
        return success(response, "Listado de roles");
    }

    @Operation(summary = "Obtener un rol por ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> findById(@PathVariable Long id) {
        RoleResponseDto response = roleService.findById(id);
        return success(response, "Rol encontrado");
    }

    // 🔹 GESTIÓN

    @Operation(summary = "Actualizar la descripción de un rol")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> update(
            @PathVariable Long id,
            @RequestBody @Valid RoleUpdateRequestDto dto) {
        RoleResponseDto response = roleService.updateDescription(id, dto);
        return success(response, "Descripción del rol actualizada correctamente");
    }
}
