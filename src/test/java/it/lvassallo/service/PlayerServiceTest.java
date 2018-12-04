package it.lvassallo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.lvassallo.GooseGameApp;
import it.lvassallo.domain.Player;
import it.lvassallo.repository.PlayerRepository;

@SpringBootTest(classes = GooseGameApp.class)
public class PlayerServiceTest {

	private PlayerService playerService;

	private PlayerRepository playerRepo = mock(PlayerRepository.class);

	private String playerName = "Pippo";

	@Before
	public void setup() {
		playerService = new PlayerService(playerRepo);
	}

	@Test
	public void testAddNewPlayerThatNotExists() {
		when(playerRepo.findOneByName(playerName)).thenReturn(Optional.empty());
		Player player = new Player().name(playerName).position(0);
		when(playerRepo.save(any())).thenReturn(player);
		List<Player> players = new ArrayList<>();
		players.add(player);
		when(playerRepo.findAll()).thenReturn(players);
		String message = playerService.addNewPlayer(playerName);
		verify(playerRepo).findOneByName(playerName);
		verify(playerRepo).save(any());
		verify(playerRepo).findAll();
		assertThat(message).isEqualTo("players: " + playerName);
	}

	@Test
	public void testAddNewPlayerThatExists() {
		Player player = new Player().name(playerName).position(0);
		when(playerRepo.findOneByName(playerName)).thenReturn(Optional.of(player));
		String message = playerService.addNewPlayer(playerName);
		assertThat(message).isEqualTo(playerName + ": already existing player");
	}

}
