package indimetra.modelo.service.Favorite;

import java.util.List;

import indimetra.modelo.entity.Favorite;
import indimetra.modelo.service.Base.IGenericDtoService;
import indimetra.modelo.service.Favorite.Model.FavoriteRequestDto;
import indimetra.modelo.service.Favorite.Model.FavoriteResponseDto;

/**
 * Servicio para la gestión de favoritos (relación entre usuario y
 * cortometraje).
 */
public interface IFavoriteService extends IGenericDtoService<Favorite, FavoriteRequestDto, FavoriteResponseDto, Long> {

    // ============================================================
    // 🔍 BÚSQUEDA Y LECTURA
    // ============================================================

    /**
     * Busca todos los favoritos de un usuario por su ID.
     *
     * @param userId ID del usuario
     * @return lista de entidades Favorite
     */
    List<Favorite> findByUserId(Long userId);

    /**
     * Busca todos los favoritos de un usuario por su nombre.
     *
     * @param username nombre del usuario
     * @return lista de entidades Favorite
     */
    List<Favorite> findByUsername(String username);

    /**
     * Obtiene todos los favoritos visibles de un usuario en formato DTO.
     *
     * @param username nombre del usuario
     * @return lista de favoritos como DTO
     */
    List<FavoriteResponseDto> findAllByUsername(String username);

    /**
     * Verifica si el usuario es propietario del favorito de un cortometraje.
     *
     * @param userId         ID del usuario
     * @param cortometrajeId ID del cortometraje
     * @return true si el favorito existe y pertenece al usuario
     */
    boolean isFavoriteOwner(Long userId, Long cortometrajeId);

    // ============================================================
    // ➕ CREACIÓN Y RESTAURACIÓN
    // ============================================================

    /**
     * Añade un nuevo favorito o restaura uno eliminado previamente.
     *
     * @param dto      DTO con el ID del cortometraje
     * @param username nombre del usuario autenticado
     * @return DTO del favorito creado o restaurado
     */
    FavoriteResponseDto addOrRestoreFavorite(FavoriteRequestDto dto, String username);

    // ============================================================
    // 🗑️ ELIMINACIÓN
    // ============================================================

    /**
     * Elimina un favorito si el usuario autenticado es su propietario o tiene rol
     * administrador.
     *
     * @param id       ID del favorito
     * @param username nombre del usuario autenticado
     */
    void deleteFavoriteIfOwnerOrAdmin(Long id, String username);
}
