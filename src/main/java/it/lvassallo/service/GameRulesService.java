package it.lvassallo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.lvassallo.domain.Player;

@Service
public class GameRulesService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String rolls_standard = "%s rolls %d, %d. ";

	private final String rolls_position = "%s moves from %s to %d";

	private final String rolls_win = "%s moves from %d to %d. %s Wins!!";

	private final String rolls_excess = "%s moves from %d to 63. %s bounces! %s returns to %d";

	private final String rolls_bridge = "%s moves from %s to The Bridge. %s jumps to 12";

	private final String rolls_goose_single = "%s moves from %s to %d, The Goose. ";

	private final String rolls_goose_multiple = "%s moves again and goes to %d, The Goose. ";

	private final String rolls_goose_final = "%s moves again and goes to %d";

	private final int WIN_NUM = 63;
	private final int THE_BRIDGE = 6;
	private final List<Integer> THE_GOOSE = new ArrayList<>(Arrays.asList(5, 9, 14, 18, 23, 27));

	private final PlayerService playerService;

	public GameRulesService(final PlayerService _playerService) {
		playerService = _playerService;
	}

	public String calculateAndPersistNewPosition(String playerName, int die1, int die2) {
		List<Player> players = playerService.findAll();

		Player playerSelected = selectPlayerByName(players, playerName);

		String standard = String.format(rolls_standard, playerName, die1, die2);
		int position = playerSelected.getPosition();
		log.info("Init position: {} and Moves: dado1: {} - dado2: {}", position, die1, die2);
		int total = position + die1 + die2;
		if (isTotalEqualToWin(total)) {
			log.info("Case: WIN");
			playerService.save(playerSelected.position(total));
			return standard + String.format(rolls_win, playerName, position, total, playerName);
		}

		if (isTotalGreaterThenLimit(total)) {
			log.info("Case: Greather than to winning position");
			int excessValue = total - WIN_NUM;
			int newPosition = WIN_NUM - excessValue;
			playerService.save(playerSelected.position(newPosition));
			return standard + String.format(rolls_excess, playerName, position, playerName, playerName, newPosition);
		}

		if (isTotalEqualToTheBridge(total)) {
			log.info("Case: The Bridge");
			playerService.save(playerSelected.position(total * 2));
			return standard + String.format(rolls_bridge, playerName, getStrOfInitPosition(position), playerName);
		}

		if (isTotalIntoTheGoose(total)) {
			log.info("Case: The Goose");
			StringBuilder sb = new StringBuilder(standard);
			String msgPartial = processTheGoose(playerSelected, playerName, die1, die2, total, sb);
			return msgPartial + String.format(rolls_goose_final, playerName, playerSelected.getPosition());
		}

		log.info("Case: Rolls");
		playerService.save(playerSelected.position(total));
		String strInitPosition = getStrOfInitPosition(position);
		return standard + String.format(rolls_position, playerName, strInitPosition, total);
	}

	private Player selectPlayerByName(List<Player> players, String playerName) {
		for (Player player : players) {
			if (player.getName().equals(playerName)) {
				return player;
			}
		}
		return null;
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

	private String processTheGoose(Player player, String playerName, int die1, int die2, int total, StringBuilder sb) {
		int increment = die1 + die2;
		int newTotal = total + increment;
		if (sb.toString().contains("moves from")) {
			sb.append(String.format(rolls_goose_multiple, playerName, total));
		} else {
			String strInitPosition = getStrOfInitPosition(player.getPosition());
			sb.append(String.format(rolls_goose_single, playerName, strInitPosition, total));
		}

		player = playerService.save(player.position(newTotal));

		if (isTotalIntoTheGoose(newTotal)) {
			processTheGoose(player, playerName, die1, die2, newTotal, sb);
		}
		return sb.toString();
	}

	private String getStrOfInitPosition(int position) {
		return (position == 0) ? "Start" : String.valueOf(position);
	}
}
