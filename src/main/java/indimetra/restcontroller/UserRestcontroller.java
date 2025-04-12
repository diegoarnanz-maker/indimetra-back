package indimetra.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import indimetra.modelo.service.Shared.Model.PagedResponse;
import indimetra.modelo.service.User.IUserService;
import indimetra.modelo.service.User.Model.UserChangePasswordDto;
import indimetra.modelo.service.User.Model.UserProfileUpdateDto;
import indimetra.modelo.service.User.Model.UserResponseDto;
import indimetra.restcontroller.base.BaseRestcontroller;
import indimetra.utils.ApiResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserRestcontroller extends BaseRestcontroller {

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findAll() {
        List<UserResponseDto> response = userService.findAll();
        return success(response, "Listado de usuarios");
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponseDto>>> findAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<UserResponseDto> pagedResponse = userService.findAllPaginated(page, size);
        return success(pagedResponse, "Listado paginado de usuarios");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> findById(@PathVariable Long id) {
        UserResponseDto response = userService.findById(id);
        return success(response, "Usuario encontrado");
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getUserStatsByRole() {
        Map<String, Integer> stats = userService.getUserCountByRole();
        return success(stats, "Estadísticas de usuarios por rol");
    }

    @GetMapping("/buscar/by-role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findByRole(@PathVariable String role) {
        List<UserResponseDto> usuarios = userService.findByRole(role);
        return success(usuarios, "Usuarios con rol: " + role);
    }

    @GetMapping("/buscar/by-username/{username}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findByUsernameContains(@PathVariable String username) {
        List<UserResponseDto> usuarios = userService.findByUsernameContains(username);
        return success(usuarios, "Usuarios que contienen en su nombre: " + username);
    }

    @PutMapping("/toggle-role/{id}")
    public ResponseEntity<ApiResponse<Void>> toggleUserRole(@PathVariable Long id) {
        if (!isAdmin()) {
            return failure("No tienes permisos para realizar esta acción");
        }

        userService.toggleRole(id);
        return success(null, "Rol del usuario actualizado correctamente");
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateMyProfile(
            @RequestBody @Valid UserProfileUpdateDto dto) {

        UserResponseDto updated = userService.updateProfile(getUsername(), dto);
        return success(updated, "Perfil actualizado correctamente");
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody @Valid UserChangePasswordDto dto) {

        userService.changePassword(getUsername(), dto);
        return success(null, "Contraseña actualizada correctamente");
    }

    // FALTA PONER deleteMyAccount

}
