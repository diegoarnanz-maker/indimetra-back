package indimetra.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import indimetra.modelo.entity.Favorite;

public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

}
