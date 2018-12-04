package it.lvassallo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.lvassallo.domain.Player;
import it.lvassallo.repository.PlayerRepository;

@Service
@Transactional
public class PlayerService {

	private final Logger log = LoggerFactory.getLogger(PlayerService.class);

	private final PlayerRepository playerRepo;

	public PlayerService(final PlayerRepository _playerRepo) {
		playerRepo = _playerRepo;
	}

	public String addNewPlayer(String playerName) {
		StringBuilder msgReturn = new StringBuilder("");
		log.debug("Check esists player {}", playerName);
		Optional<Player> playerOpt = playerRepo.findOneByName(playerName);
		if (playerOpt.isPresent()) {
			msgReturn.append(playerName + ": already existing player");
		} else {
			playerRepo.save(new Player().name(playerName).position(0));
			List<Player> players = playerRepo.findAll();
			int i = 0;
			msgReturn.append("players:");
			for (Player player : players) {
				if (i > 0) {
					msgReturn.append(",");
				}
				msgReturn.append(" " + player.getName());
				i++;
			}
		}

		return msgReturn.toString();
	}

	public Player findByName(String namePlayer) throws Exception {
		Optional<Player> playerOpt = playerRepo.findOneByName(namePlayer);
		if (playerOpt.isPresent()) {
			return playerOpt.get();
		}
		throw new Exception("Not found player by name: " + namePlayer);
	}

	public Player save(Player player) {
		log.debug("Persist player: {}", player);
		return playerRepo.save(player);
	}

	public List<Player> findAll() {
		return playerRepo.findAll();
	}

}
