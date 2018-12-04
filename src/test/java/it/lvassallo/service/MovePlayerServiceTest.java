package it.lvassallo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.lvassallo.GooseGameApp;
import it.lvassallo.domain.Player;
import it.lvassallo.service.dto.MovePlayerDTO;

@SpringBootTest(classes = GooseGameApp.class)
public class MovePlayerServiceTest {

	private MovePlayerService movePlayerService;
	private PlayerService playerService = mock(PlayerService.class);

	private String playerName = "Pippo";

	@Before
	public void setup() {
		movePlayerService = new MovePlayerService(playerService);
	}

	@Test
	public void testNeedRandomValueToDadi() throws Exception {
		MovePlayerDTO movePlayerDTO = new MovePlayerDTO(playerName);
		Player player = new Player().name(playerName).position(0);
		when(playerService.findByName(playerName)).thenReturn(player);
		when(playerService.save(player)).thenReturn(player.position(1));
		movePlayerService.movePlayer(movePlayerDTO);

		assertThat(movePlayerDTO.getDado1()).isNotEqualTo(0);
		assertThat(movePlayerDTO.getDado1()).isGreaterThan(0);
		assertThat(movePlayerDTO.getDado1()).isLessThan(7);

		assertThat(movePlayerDTO.getDado2()).isNotEqualTo(0);
		assertThat(movePlayerDTO.getDado2()).isGreaterThan(0);
		assertThat(movePlayerDTO.getDado2()).isLessThan(7);
	}

}
