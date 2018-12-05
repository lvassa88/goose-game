package it.lvassallo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.lvassallo.domain.Player;

public class GameRulesServiceTest {

	private PlayerService playerServiceMock = mock(PlayerService.class);

	private GameRulesService gameRulesService;

	private final String playerName = "Pippo";

	private List<Player> players = null;

	@Before
	public void setup() {
		gameRulesService = new GameRulesService(playerServiceMock);
		players = new ArrayList<>();
	}

	@Test
	public void testRuleScenarioStart() {
		int die1 = 1;
		int die2 = 2;
		int startPosition = 0;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock).save(players.get(0).position(startPosition + die1 + die2));
		assertThat(msg).isEqualTo(playerName + " rolls 1, 2. " + playerName + " moves from Start to 3");
	}

	@Test
	public void testRuleScenarioDiceRoll() {
		int die1 = 4;
		int die2 = 2;
		int startPosition = 2;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock).save(players.get(0).position(startPosition + die1 + die2));
		assertThat(msg).isEqualTo(playerName + " rolls 4, 2. " + playerName + " moves from " + startPosition + " to 8");
	}

	@Test
	public void testRuleScenarioWin() {
		int die1 = 2;
		int die2 = 1;
		int startPosition = 60;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock).save(players.get(0).position(startPosition + die1 + die2));
		assertThat(msg).isEqualTo(playerName + " rolls 2, 1. " + playerName + " moves from " + startPosition
				+ " to 63. " + playerName + " Wins!!");
	}

	@Test
	public void testRuleScenarioExcessLimitToWin() {
		int die1 = 5;
		int die2 = 3;
		int startPosition = 60;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock).save(players.get(0).position(startPosition + die1 + die2));
		assertThat(msg).isEqualTo(playerName + " rolls 5, 3. " + playerName + " moves from " + startPosition
				+ " to 63. " + playerName + " bounces! " + playerName + " returns to 58");
	}

	@Test
	public void testRuleScenarioTheBridge() {
		int die1 = 2;
		int die2 = 1;
		int startPosition = 3;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock).save(players.get(0).position(startPosition + die1 + die2));
		assertThat(msg).isEqualTo(playerName + " rolls 2, 1. " + playerName + " moves from " + startPosition
				+ " to The Bridge. " + playerName + " jumps to 12");
	}

	@Test
	public void testRuleScenarioTheGooseSimple() {
		int die1 = 1;
		int die2 = 1;
		int startPosition = 3;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		when(playerServiceMock.save(any())).thenReturn(players.get(0));
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock).save(any());
		assertThat(msg).isEqualTo(playerName + " rolls 1, 1. " + playerName + " moves from " + startPosition
				+ " to 5, The Goose. " + playerName + " moves again and goes to 7");
	}

	@Test
	public void testRuleScenarioTheGooseMultiple001() {
		int die1 = 2;
		int die2 = 2;
		int startPosition = 10;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		when(playerServiceMock.save(any())).thenReturn(players.get(0));
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock, times(1)).save(any());
		assertThat(msg).isEqualTo(playerName + " rolls 2, 2. " + playerName + " moves from " + startPosition
				+ " to 14, The Goose. " + playerName + " moves again and goes to 18, The Goose. " + playerName
				+ " moves again and goes to 22");
	}

	@Test
	public void testRuleScenarioTheGooseMultiple002() {
		int die1 = 5;
		int die2 = 4;
		int startPosition = 0;
		players.add(new Player().name(playerName).position(startPosition));
		when(playerServiceMock.findAll()).thenReturn(players);
		when(playerServiceMock.save(any())).thenReturn(players.get(0));
		String msg = gameRulesService.calculateAndPersistNewPosition(playerName, die1, die2);
		System.out.println(msg);

		verify(playerServiceMock).findAll();
		verify(playerServiceMock, times(1)).save(any());
		assertThat(msg).isEqualTo(playerName + " rolls 5, 4. " + playerName + " moves from Start to 9, The Goose. " + playerName + " moves again and goes to 18, The Goose. " + playerName
				+ " moves again and goes to 27, The Goose. " + playerName + " moves again and goes to 36");
	}
}
