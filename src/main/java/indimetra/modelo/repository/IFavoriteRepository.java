package indimetra.modelo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Favorite;

public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    List<Favorite> findByUserUsername(String username);

    Optional<Favorite> findByUserIdAndCortometrajeId(Long userId, Long cortometrajeId);

    List<Favorite> findByCortometrajeId(Long cortometrajeId);

    // Filtrados por isActive e isDeleted

    List<Favorite> findByUserIdAndIsActiveTrueAndIsDeletedFalse(Long userId);

}
