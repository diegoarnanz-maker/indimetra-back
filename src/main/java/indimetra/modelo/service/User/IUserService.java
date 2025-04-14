package indimetra.modelo.service.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import indimetra.modelo.entity.User;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Shared.Model.PagedResponse;
import indimetra.modelo.service.User.Model.UserChangePasswordDto;
import indimetra.modelo.service.User.Model.UserProfileUpdateDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;

public interface IUserService extends IGenericDtoService<User, UserRequestDto, UserResponseDto, Long> {

    void updateAuthorStatus(Long userId, boolean isAuthor);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void changePassword(String username, UserChangePasswordDto dto);

    // void toggleUserStatus(Long userId, boolean enabled);

    List<UserResponseDto> findByRole(String role);

    UserResponseDto updateProfile(String username, UserProfileUpdateDto dto);

    void deleteIfNotAdmin(Long id, String username);

    PagedResponse<UserResponseDto> findAllPaginated(int page, int size);

    void toggleRole(Long userId);

    List<UserResponseDto> findByUsernameContains(String username);

    Map<String, Integer> getUserCountByRole();

    void setUserActiveStatus(Long userId, boolean isActive);

    void reactivateUser(Long userId);

    PagedResponse<UserResponseDto> findActiveUsersPaginated(int page, int size);

    void softDeleteUser(Long id, String currentUsername);

    void deleteMyAccount(String username);

}
