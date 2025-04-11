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

    //FALTA PARA ADMIN PODER CAMBIAR A UN USUARIO ROLE_USER A ROLE_ADMIN
    // @PutMapping("/change-role/{id}")

    //FALTA PODER deleteMyAccount

    //FILTRAR USUARIOS POR ROLE / USERNAME

}
