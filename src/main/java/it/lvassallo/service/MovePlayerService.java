package it.lvassallo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.lvassallo.service.dto.MovePlayerDTO;

@Service
public class MovePlayerService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final GameRulesService gameRulesService;

	public MovePlayerService(final GameRulesService _gameRulesService) {
		gameRulesService = _gameRulesService;
	}

	public String movePlayer(MovePlayerDTO movePlayerDTO) {
		StringBuilder sb = new StringBuilder("");
		if (isNeedLaunchDie(movePlayerDTO)) {
			setNewValueToDadi(movePlayerDTO);
		}
		try {
			String playerName = movePlayerDTO.getPlayerName();
			int die1 = movePlayerDTO.getDado1();
			int die2 = movePlayerDTO.getDado2();
			String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);
			sb.append(msg);
		} catch (Exception e) {
			log.error("Exception:", e);
			sb.append("Not found player: " + movePlayerDTO.getPlayerName());
		}
		return sb.toString();
	}

	private boolean isNeedLaunchDie(MovePlayerDTO movePlayerDTO) {
		return ((movePlayerDTO.getDado1() == 0 || movePlayerDTO.getDado2() == 0));
	}

	private void setNewValueToDadi(MovePlayerDTO movePlayerDTO) {
		movePlayerDTO.setDado1(randomIntBetween1and6());
		movePlayerDTO.setDado2(randomIntBetween1and6());
	}

	private int randomIntBetween1and6() {
		int num = 0;
		while (num < 1 || num > 6) {
			num = (int) (Math.random() * 7);
		}
		return num;
	}

}
