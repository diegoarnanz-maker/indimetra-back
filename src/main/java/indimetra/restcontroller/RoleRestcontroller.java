package indimetra.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indimetra.modelo.service.Role.IRoleService;
import indimetra.modelo.service.Role.Model.RoleResponseDto;
import indimetra.modelo.service.Role.Model.RoleUpdateRequestDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/role")
public class RoleRestcontroller extends BaseRestcontroller {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> findAll() {
        List<RoleResponseDto> response = roleService.findAll();
        return success(response, "Listado de roles");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> findById(@PathVariable Long id) {
        RoleResponseDto response = roleService.findById(id);
        return success(response, "Rol encontrado");
    }

    // @PostMapping
    // public ResponseEntity<ApiResponse<RoleResponseDto>> create(@RequestBody
    // @Valid RoleRequestDto dto) {
    // RoleResponseDto response = roleService.create(dto);
    // return created(response, "Rol creado correctamente");
    // }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> update(
            @PathVariable Long id,
            @RequestBody @Valid RoleUpdateRequestDto dto) {
        RoleResponseDto response = roleService.updateDescription(id, dto);
        return success(response, "Descripción del rol actualizada correctamente");
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
    // roleService.delete(id);
    // return success(null, "Rol eliminado correctamente");
    // }

}
