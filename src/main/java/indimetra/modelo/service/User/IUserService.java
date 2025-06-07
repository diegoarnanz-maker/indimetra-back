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

/**
 * Servicio de gestión de usuarios con soporte para DTOs.
 */
public interface IUserService extends IGenericDtoService<User, UserRequestDto, UserResponseDto, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return un Optional con el usuario si existe
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por su email.
     *
     * @param email correo electrónico del usuario
     * @return un Optional con el usuario si existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca usuarios cuyo nombre de usuario contenga un texto.
     *
     * @param username texto parcial del nombre de usuario
     * @return lista de usuarios coincidentes
     */
    List<UserResponseDto> findByUsernameContains(String username);

    /**
     * Devuelve todos los usuarios con un rol específico.
     *
     * @param role nombre del rol (por ejemplo, "ROLE_ADMIN")
     * @return lista de usuarios con ese rol
     */
    List<UserResponseDto> findByRole(String role);

    /**
     * Obtiene los usuarios en formato paginado.
     *
     * @param page número de página
     * @param size tamaño de página
     * @return respuesta paginada con usuarios
     */
    PagedResponse<UserResponseDto> findAllPaginated(int page, int size);

    /**
     * Obtiene únicamente los usuarios activos en formato paginado.
     *
     * @param page número de página
     * @param size tamaño de página
     * @return respuesta paginada con usuarios activos
     */
    PagedResponse<UserResponseDto> findActiveUsersPaginated(int page, int size);

    /**
     * Obtiene únicamente los usuarios eliminados en formato paginado.
     *
     * @param page número de página
     * @param size tamaño de página
     * @return respuesta paginada con usuarios eliminados
     */
    PagedResponse<UserResponseDto> findInactiveUsersPaginated(int page, int size);

    /**
     * Obtiene únicamente los usuarios eliminados en formato paginado.
     *
     * @param page número de página
     * @param size tamaño de página
     * @return respuesta paginada con usuarios eliminados
     */
    PagedResponse<UserResponseDto> findDeletedUsersPaginated(int page, int size);

    /**
     * Devuelve un mapa con el número de usuarios por cada rol.
     *
     * @return mapa donde la clave es el rol y el valor es la cantidad de usuarios
     */
    Map<String, Integer> getUserCountByRole();

    // ============================================================
    // 🔧 ACTUALIZACIÓN Y GESTIÓN
    // ============================================================

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param username nombre de usuario autenticado
     * @param dto      datos del perfil a actualizar
     * @return DTO actualizado del usuario
     */
    UserResponseDto updateProfile(String username, UserProfileUpdateDto dto);

    /**
     * Cambia la contraseña del usuario autenticado.
     *
     * @param username nombre de usuario autenticado
     * @param dto      nueva contraseña
     */
    void changePassword(String username, UserChangePasswordDto dto);

    /**
     * Actualiza el estado de autor de un usuario (true/false).
     *
     * @param userId   ID del usuario
     * @param isAuthor nuevo estado del campo isAuthor
     */
    void updateAuthorStatus(Long userId, boolean isAuthor);

    /**
     * Alterna entre "ROLE_USER" y "ROLE_ADMIN" para un usuario.
     *
     * @param userId ID del usuario
     */
    void toggleRole(Long userId);

    /**
     * Activa o desactiva a un usuario.
     *
     * @param userId   ID del usuario
     * @param isActive nuevo estado de activación
     */
    void setUserActiveStatus(Long userId, boolean isActive);

    // ============================================================
    // 🗑️ ELIMINACIÓN Y RESTAURACIÓN
    // ============================================================


    /**
     * Realiza una eliminación lógica del usuario (soft delete).
     *
     * @param id              ID del usuario
     * @param currentUsername usuario que ejecuta la acción
     */
    void softDeleteUser(Long id, String currentUsername);

    /**
     * Elimina la cuenta del usuario autenticado.
     *
     * @param username nombre de usuario
     */
    void deleteMyAccount(String username);

    /**
     * Reactiva a un usuario desactivado.
     *
     * @param userId ID del usuario a reactivar
     */
    void reactivateUser(Long userId);
}
