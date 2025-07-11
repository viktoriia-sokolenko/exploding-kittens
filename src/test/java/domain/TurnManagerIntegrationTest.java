package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnManagerIntegrationTest {
	private TurnManager turnManager;
	private Deck deck;
	private PlayerManager playerManager;
	private static final int NUM_CARDS = 20;
	private static final int DEFAULT_NUM_PLAYERS = 3;

	@BeforeEach
	void setUp() {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < NUM_CARDS; i++) {
			cards.add(new SkipCard());
		}
		deck = new Deck(cards);
		turnManager = new TurnManager();

		playerManager = new PlayerManager(deck);
		playerManager.addPlayers(DEFAULT_NUM_PLAYERS);
	}

	@Test
	void constructor_withValidDeck_initializesState() {
		assertNotNull(turnManager);
		assertThrows(IllegalStateException.class, () -> {
			turnManager.getCurrentActivePlayer();
		});
	}

	@Test
	void setPlayerManager_withNullPlayerManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			turnManager.setPlayerManager(null);
		});
	}

	@Test
	void setPlayerManager_withEmptyPlayerList_throwsIllegalArgumentException() {
		PlayerManager emptyPM = new PlayerManager(deck);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> turnManager.setPlayerManager(emptyPM)
		);

		assertTrue(exception.getMessage().contains("No players provided"));
	}

	@Test
	void setPlayerManager_withValidPlayers_initializesCurrentPlayer() {
		turnManager.setPlayerManager(playerManager);

		Player currentPlayer = turnManager.getCurrentActivePlayer();
		assertNotNull(currentPlayer);

		List<Player> players = playerManager.getPlayers();
		assertEquals(players.get(0), currentPlayer);
	}

	@Test
	void getCurrentActivePlayer_beforeSetup_throwsIllegalStateException() {
		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> turnManager.getCurrentActivePlayer()
		);

		assertTrue(exception.getMessage().contains("TurnManager not initialized"));
	}

	@Test
	void getCurrentActivePlayer_afterSetup_returnsFirstPlayer() {
		turnManager.setPlayerManager(playerManager);
		List<Player> players = playerManager.getPlayers();
		Player current = turnManager.getCurrentActivePlayer();
		assertEquals(players.get(0), current);
	}

	@Test
	void endTurnWithoutDraw_beforeSetup_throwsIllegalStateException() {
		assertThrows(IllegalStateException.class, () -> {
			turnManager.endTurnWithoutDraw();
		});
	}

	@Test
	void endTurnWithoutDraw_withTwoPlayers_advancesToNextPlayer() {
		PlayerManager twoPlayerManager = new PlayerManager(deck);
		twoPlayerManager.addPlayers(2);
		turnManager.setPlayerManager(twoPlayerManager);

		List<Player> players = twoPlayerManager.getPlayers();
		Player firstPlayer = players.get(0);
		Player secondPlayer = players.get(1);

		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());
		turnManager.endTurnWithoutDraw();
		assertEquals(secondPlayer, turnManager.getCurrentActivePlayer());
	}

	@Test
	void endTurnWithoutDraw_withOnePlayer_staysOnSamePlayer() {
		PlayerManager onePlayerManager = new PlayerManager(deck);
		onePlayerManager.addPlayers(2);
		turnManager.setPlayerManager(onePlayerManager);

		List<Player> players = onePlayerManager.getPlayers();
		onePlayerManager.removePlayerFromGame(players.get(1));
		turnManager.syncWith(onePlayerManager.getActivePlayers());

		Player remainingPlayer = onePlayerManager.getActivePlayers().get(0);
		turnManager.endTurnWithoutDraw();
		assertEquals(remainingPlayer, turnManager.getCurrentActivePlayer());
	}

	@Test
	void addTurnForCurrentPlayer_beforeSetup_throwsIllegalStateException() {
		assertThrows(IllegalStateException.class, () -> {
			turnManager.addTurnForCurrentPlayer();
		});
	}

	@Test
	void addTurnForCurrentPlayer_withValidSetup_duplicatesCurrentPlayerInQueue() {
		turnManager.setPlayerManager(playerManager);
		List<Player> players = playerManager.getPlayers();
		Player firstPlayer = players.get(0);
		Player secondPlayer = players.get(1);

		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());

		turnManager.addTurnForCurrentPlayer();
		turnManager.endTurnWithoutDraw();
		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());

		turnManager.endTurnWithoutDraw();
		assertEquals(secondPlayer, turnManager.getCurrentActivePlayer());
	}

	@Test
	void syncWith_withNullList_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			turnManager.syncWith(null);
		});
	}

	@Test
	void syncWith_withEmptyList_throwsIllegalArgumentException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> turnManager.syncWith(new ArrayList<>())
		);

		assertTrue(exception.getMessage().contains("No players provided"));
	}

	@Test
	void syncWith_withActivePlayers_updatesQueue() {
		turnManager.setPlayerManager(playerManager);
		List<Player> allPlayers = playerManager.getPlayers();

		playerManager.removePlayerFromGame(allPlayers.get(1));
		List<Player> activePlayers = playerManager.getActivePlayers();

		turnManager.syncWith(activePlayers);

		Player current = turnManager.getCurrentActivePlayer();
		assertTrue(activePlayers.contains(current));
	}

	@Test
	void getTurnOrder_beforeSetup_throwsIllegalStateException() {
		assertThrows(IllegalStateException.class, () -> {
			turnManager.getTurnOrder();
		});
	}

	@Test
	void getTurnOrder_afterSetup_returnsCorrectOrder() {
		turnManager.setPlayerManager(playerManager);
		List<Player> expectedPlayers = playerManager.getPlayers();

		List<Player> turnOrder = turnManager.getTurnOrder();
		assertEquals(expectedPlayers.size(), turnOrder.size());
		assertEquals(expectedPlayers.get(0), turnOrder.get(0));
		assertEquals(expectedPlayers.get(1), turnOrder.get(1));
		assertEquals(expectedPlayers.get(2), turnOrder.get(2));
	}
}
