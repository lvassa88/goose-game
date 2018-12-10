package it.lvassallo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.lvassallo.GooseGameApp;
import it.lvassallo.service.dto.MovePlayerDTO;

@SpringBootTest(classes = GooseGameApp.class)
public class MovePlayerServiceTest {

	private MovePlayerService movePlayerService;
	private GameRulesService gameRulesService = mock(GameRulesService.class);

	private String playerName = "Pippo";

	@Before
	public void setup() {
		movePlayerService = new MovePlayerService(gameRulesService);
	}

	@Test
	public void testNeedRandomValueToDadi() throws Exception {
		MovePlayerDTO movePlayerDTO = new MovePlayerDTO(playerName);
		when(gameRulesService.calculateAndPersistNewPosition(playerName, 0, 0)).thenReturn("OK");
		movePlayerService.movePlayer(movePlayerDTO);

		assertThat(movePlayerDTO.getDado1()).isNotEqualTo(0);
		assertThat(movePlayerDTO.getDado1()).isGreaterThan(0);
		assertThat(movePlayerDTO.getDado1()).isLessThan(7);

		assertThat(movePlayerDTO.getDado2()).isNotEqualTo(0);
		assertThat(movePlayerDTO.getDado2()).isGreaterThan(0);
		assertThat(movePlayerDTO.getDado2()).isLessThan(7);
	}

}
