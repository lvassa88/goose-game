package it.lvassallo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.lvassallo.domain.Player;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

	Optional<Player> findOneByName(String name);

	List<Player> findAll();

}
