package it.lvassallo.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import it.lvassallo.service.MovePlayerService;
import it.lvassallo.service.PlayerService;
import it.lvassallo.service.dto.MovePlayerDTO;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

	private final Logger log = LoggerFactory.getLogger(GameResource.class);

	private final static String ADD_PLAYER = "add player";

	private final static String MOVE = "move";

	private final PlayerService playerService;
	private final MovePlayerService movePlayerService;

	public GameResource(final PlayerService _playerService, final MovePlayerService _movePlayerService) {
		playerService = _playerService;
		movePlayerService = _movePlayerService;
	}

	@PostMapping("/game")
	@Timed
	public ResponseEntity<String> game(@Valid @RequestParam String command) {
		if (StringUtils.isBlank(command)) {
			return ResponseEntity.badRequest().body("Command is null or empty");
		}

		String msg = null;
		if (StringUtils.startsWith(command, ADD_PLAYER)) {
			msg = playerService.addNewPlayer(filterToNewPlayer(command));
		} else if (StringUtils.startsWith(command, MOVE)) {
			MovePlayerDTO movePlayerDto = filterToMovePlayer(command);
			msg = movePlayerService.movePlayer(movePlayerDto);
		} else {
			return ResponseEntity.badRequest().body("Command is not valid...");
		}

		return ResponseEntity.ok(msg);
	}

	private String filterToNewPlayer(String command) {
		String replace = StringUtils.replace(command, ADD_PLAYER, "");
		return replace.trim();
	}

	private MovePlayerDTO filterToMovePlayer(String command) {
		String replace = StringUtils.replace(command, MOVE, "");
		replace.trim();

		List<String> moveCommandInfo = new ArrayList<>();
		StringTokenizer strTok = new StringTokenizer(replace, " ");
		while (strTok.hasMoreTokens()) {
			String token = strTok.nextToken();
			if (token.contains(",")) {
				token = StringUtils.replace(token, ",", "");
			}
			moveCommandInfo.add(token.trim());
		}

		MovePlayerDTO movePlayerDTO = null;
		if (moveCommandInfo.size() > 1) {
			String name = moveCommandInfo.get(0);
			int dado1 = Integer.parseInt(moveCommandInfo.get(1));
			int dado2 = Integer.parseInt(moveCommandInfo.get(2));
			movePlayerDTO = new MovePlayerDTO(name, dado1, dado2);
		} else {
			movePlayerDTO = new MovePlayerDTO(moveCommandInfo.get(0));
		}
		return movePlayerDTO;
	}

}
