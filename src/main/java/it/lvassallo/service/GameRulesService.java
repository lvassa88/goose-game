package it.lvassallo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.lvassallo.domain.Player;

@Service
public class GameRulesService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String rolls_standard = "%s rolls %d, %d. ";

	private final String rolls_position = "%s moves from %d to %d";
	private final String rolls_position_start = "%s moves from Start to %d";

	private final String rolls_win = "%s moves from %d to %d. %s Wins!!";

	private final String rolls_excess = "%s moves from %d to 63. %s bounces! %s returns to %d";

	private final String rolls_bridge = "%s moves from %d to The Bridge. %s jumps to 12";

	private final String rolls_goose_single = "%s moves from %d to %d, The Goose. %s moves again and goes to %d";

	private final String rolls_goose_multiple = "%s moves from %d to %d, The Goose. %s moves again and goes to %d, The Goose. Pippo moves again and goes to %d";

	private final int WIN_NUM = 63;
	private final int THE_BRIDGE = 6;
	private final List<Integer> THE_GOOSE = new ArrayList<>(Arrays.asList(5, 9, 14, 18, 23, 27));

	private final PlayerService playerService;

	public GameRulesService(final PlayerService _playerService) {
		playerService = _playerService;
	}

	private Optional<Player> getPlayerInEqualPosition(List<Player> players, int positionReference) {
		return players.stream().filter(p -> p.getPosition() == positionReference).findFirst();
	}

	private boolean isTotalIntoTheGoose(int total) {
		return THE_GOOSE.stream().filter(i -> i == total).findFirst().isPresent();
	}

	private boolean isTotalEqualToTheBridge(int total) {
		return total == THE_BRIDGE;
	}

	private boolean isTotalEqualToWin(int total) {
		return total == WIN_NUM;
	}

	private boolean isTotalGreaterThenLimit(int total) {
		return total > WIN_NUM;
	}

	public String calculateAndPersistNewPosition(String playerName, int die1, int die2) {
		List<Player> players = playerService.findAll();
		Player playerSelected = null;
		for (Player player : players) {
			if (player.getName().equals(playerName)) {
				playerSelected = player;
			}
		}

		String standard = String.format(rolls_standard, playerName, die1, die2);

		int position = playerSelected.getPosition();
		int total = position + die1 + die2;
		if (isTotalEqualToWin(total)) {
			playerService.save(playerSelected.position(total));
			return standard + String.format(rolls_win, playerName, position, total, playerName);
		}

		if (isTotalEqualToTheBridge(total)) {
			playerService.save(playerSelected.position(total * 2));
			return standard + String.format(rolls_bridge, playerName, position, playerName);
		}

		if (isTotalGreaterThenLimit(total)) {
			int excessValue = total - WIN_NUM;
			int newPosition = WIN_NUM - excessValue;
			playerService.save(playerSelected.position(newPosition));
			return standard + String.format(rolls_excess, playerName, position, playerName, playerName, newPosition);
		}
		
//		if(isTotalIntoTheGoose(total)) {
//			return processTheGoos(playerSelected, playerName, die1, die2, total);
//		}

		playerService.save(playerSelected.position(total));
		return standard + ((position == 0) ? String.format(rolls_position_start, playerName, total)
				: String.format(rolls_position, playerName, position, total));
	}

//	private String processTheGoos(Player playerSelected, String playerName, int die1, int die2, int total) {
//		if(isTotalIntoTheGoose(total)) {
//			
//		} 
//		return ;
//	}
}
