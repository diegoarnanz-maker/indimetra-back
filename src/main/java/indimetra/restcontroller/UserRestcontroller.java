package indimetra.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import indimetra.modelo.service.Shared.Model.PagedResponse;
import indimetra.modelo.service.User.IUserService;
import indimetra.modelo.service.User.Model.UserChangePasswordDto;
import indimetra.modelo.service.User.Model.UserProfileUpdateDto;
import indimetra.modelo.service.User.Model.UserResponseDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User Controller", description = "Gestión de usuarios")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserRestcontroller extends BaseRestcontroller {

    @Autowired
    private IUserService userService;

    // ============================================
    // 🔐 ZONA ADMIN (ROLE_ADMIN)
    // ============================================

    // 🔹 LECTURA

    @Operation(summary = "Obtener todos los usuarios")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findAll() {
        List<UserResponseDto> response = userService.findAll();
        return success(response, "Listado de usuarios");
    }

    @Operation(summary = "Obtener usuarios paginados")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponseDto>>> findAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<UserResponseDto> pagedResponse = userService.findAllPaginated(page, size);
        return success(pagedResponse, "Listado paginado de usuarios");
    }

    @Operation(summary = "Obtener usuarios activos paginados")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/paginated/active")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponseDto>>> findActiveUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<UserResponseDto> pagedResponse = userService.findActiveUsersPaginated(page, size);
        return success(pagedResponse, "Listado paginado de usuarios activos");
    }

    @Operation(summary = "Obtener un usuario por ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> findById(@PathVariable Long id) {
        UserResponseDto response = userService.findById(id);
        return success(response, "Usuario encontrado");
    }

    @Operation(summary = "Obtener estadísticas de usuarios por rol")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getUserStatsByRole() {
        Map<String, Integer> stats = userService.getUserCountByRole();
        return success(stats, "Estadísticas de usuarios por rol");
    }

    @Operation(summary = "Buscar usuarios por rol")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/buscar/by-role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findByRole(@PathVariable String role) {
        List<UserResponseDto> usuarios = userService.findByRole(role);
        return success(usuarios, "Usuarios con rol: " + role);
    }

    @Operation(summary = "Buscar usuarios por nombre de usuario")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/buscar/by-username/{username}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findByUsernameContains(@PathVariable String username) {
        List<UserResponseDto> usuarios = userService.findByUsernameContains(username);
        return success(usuarios, "Usuarios que contienen en su nombre: " + username);
    }

    // 🔹 GESTIÓN

    @Operation(summary = "Alternar el rol de un usuario (admin/user)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/toggle-role/{id}")
    public ResponseEntity<ApiResponse<Void>> toggleUserRole(@PathVariable Long id) {
        userService.toggleRole(id);
        return success(null, "Rol del usuario actualizado correctamente");
    }

    @Operation(summary = "Desactivar un usuario")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        userService.setUserActiveStatus(id, false);
        return success(null, "Usuario desactivado correctamente");
    }

    @Operation(summary = "Reactivar un usuario")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/reactivate/{id}")
    public ResponseEntity<ApiResponse<Void>> reactivateUser(@PathVariable Long id) {
        userService.reactivateUser(id);
        return success(null, "Usuario reactivado correctamente");
    }

    @Operation(summary = "Eliminar lógicamente un usuario")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/soft-delete/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id, getUsername());
        return success(null, "Usuario eliminado lógicamente");
    }

    // ============================================
    // 👤 ZONA AUTENTICADO (ROLE_USER o ROLE_ADMIN)
    // ============================================

    @Operation(summary = "Actualizar el perfil del usuario autenticado")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateMyProfile(
            @RequestBody @Valid UserProfileUpdateDto dto) {
        UserResponseDto updated = userService.updateProfile(getUsername(), dto);
        return success(updated, "Perfil actualizado correctamente");
    }

    @Operation(summary = "Cambiar la contraseña del usuario autenticado")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/me/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody @Valid UserChangePasswordDto dto) {
        userService.changePassword(getUsername(), dto);
        return success(null, "Contraseña actualizada correctamente");
    }

    @Operation(summary = "Eliminar la cuenta del usuario autenticado")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/me/delete")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount() {
        userService.deleteMyAccount(getUsername());
        return success(null, "Tu cuenta ha sido eliminada correctamente");
    }
}
