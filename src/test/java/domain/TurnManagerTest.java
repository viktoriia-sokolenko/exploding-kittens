package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class TurnManagerTest {

	private TurnManager turnManager;
	private static final int DEFAULT_NUM_PLAYERS = 3;

	@BeforeEach
	void setUp() {
		Deck deck = EasyMock.createMock(Deck.class);
		turnManager = new TurnManager(deck);
	}


	@Test
	void constructor_withNullDeck_throwsNullPointerException() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> new TurnManager(null)
		);

		assertEquals("Deck cannot be null", exception.getMessage());
	}

	@Test
	void setPlayerManager_withNullPlayerManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			turnManager.setPlayerManager(null);
		});
	}

	@Test
	void setPlayerManager_withEmptyPlayerList_throwsIllegalArgumentException() {
		PlayerManager emptyPlayerManager = EasyMock.createMock(PlayerManager.class);
		List<Player> emptyPlayers = new ArrayList<>();
		EasyMock.expect(emptyPlayerManager.getPlayers()).andReturn(emptyPlayers);
		EasyMock.replay(emptyPlayerManager);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> turnManager.setPlayerManager(emptyPlayerManager)
		);
		assertTrue(exception.getMessage().contains("No players provided"));

		EasyMock.verify(emptyPlayerManager);
	}

	@Test
	void setPlayerManager_withValidPlayers_initializesCurrentPlayer() {
		PlayerManager playerManagerWithThreePlayers =
				mockPlayerManager(DEFAULT_NUM_PLAYERS);
		turnManager.setPlayerManager(playerManagerWithThreePlayers);

		Player currentPlayer = turnManager.getCurrentActivePlayer();
		assertNotNull(currentPlayer);

		List<Player> players = playerManagerWithThreePlayers.getPlayers();
		assertEquals(players.get(0), currentPlayer);

		EasyMock.verify(playerManagerWithThreePlayers);
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
		PlayerManager playerManagerWithThreePlayers =
				mockPlayerManager(DEFAULT_NUM_PLAYERS);
		turnManager.setPlayerManager(playerManagerWithThreePlayers);
		List<Player> players = playerManagerWithThreePlayers.getPlayers();
		Player current = turnManager.getCurrentActivePlayer();
		assertEquals(players.get(0), current);

		EasyMock.verify(playerManagerWithThreePlayers);
	}

	@Test
	void endTurnWithoutDraw_beforeSetup_throwsIllegalStateException() {
		assertThrows(IllegalStateException.class, () -> {
			turnManager.endTurnWithoutDraw();
		});
	}

	@Test
	void endTurnWithoutDraw_withTwoPlayers_advancesToNextPlayer() {
		PlayerManager playerManagerWithTwoPlayers = mockPlayerManager(2);
		turnManager.setPlayerManager(playerManagerWithTwoPlayers);

		List<Player> players = playerManagerWithTwoPlayers.getPlayers();
		Player firstPlayer = players.get(0);
		Player secondPlayer = players.get(1);

		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());
		turnManager.endTurnWithoutDraw();
		assertEquals(secondPlayer, turnManager.getCurrentActivePlayer());

		EasyMock.verify(playerManagerWithTwoPlayers);
	}

	@Test
	void endTurnWithoutDraw_withOnePlayer_staysOnSamePlayer() {
		PlayerManager playerManager = mockPlayerManager(2);
		turnManager.setPlayerManager(playerManager);

		Player remainingPlayer = EasyMock.createMock(Player.class);
		List<Player> remainingPlayerList = List.of(remainingPlayer);
		turnManager.syncWith(remainingPlayerList);

		turnManager.endTurnWithoutDraw();
		assertEquals(remainingPlayer, turnManager.getCurrentActivePlayer());

		EasyMock.verify(playerManager);
	}

	@Test
	void addTurnForCurrentPlayer_beforeSetup_throwsIllegalStateException() {
		assertThrows(IllegalStateException.class, () -> {
			turnManager.addTurnForCurrentPlayer();
		});
	}

	@Test
	void addTurnForCurrentPlayer_withValidSetup_duplicatesCurrentPlayerInQueue() {
		PlayerManager playerManagerWithThreePlayers =
				mockPlayerManager(DEFAULT_NUM_PLAYERS);
		turnManager.setPlayerManager(playerManagerWithThreePlayers);

		List<Player> players = playerManagerWithThreePlayers.getPlayers();
		Player firstPlayer = players.get(0);
		Player secondPlayer = players.get(1);

		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());

		turnManager.addTurnForCurrentPlayer();
		turnManager.endTurnWithoutDraw();
		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());

		turnManager.endTurnWithoutDraw();
		assertEquals(secondPlayer, turnManager.getCurrentActivePlayer());

		EasyMock.verify(playerManagerWithThreePlayers);
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
		PlayerManager playerManagerWithThreePlayers =
				mockPlayerManager(DEFAULT_NUM_PLAYERS);
		turnManager.setPlayerManager(playerManagerWithThreePlayers);

		List<Player> activePlayers = mockTwoPlayersList();
		turnManager.syncWith(activePlayers);

		Player current = turnManager.getCurrentActivePlayer();
		assertTrue(activePlayers.contains(current));

		EasyMock.verify(playerManagerWithThreePlayers);
	}

	@Test
	void getTurnOrder_beforeSetup_throwsIllegalStateException() {
		assertThrows(IllegalStateException.class, () -> {
			turnManager.getTurnOrder();
		});
	}

	@Test
	void getTurnOrder_afterSetup_returnsCorrectOrder() {
		PlayerManager playerManagerWithThreePlayers =
				mockPlayerManager(DEFAULT_NUM_PLAYERS);
		turnManager.setPlayerManager(playerManagerWithThreePlayers);

		List<Player> expectedPlayers = playerManagerWithThreePlayers.getPlayers();

		List<Player> turnOrder = turnManager.getTurnOrder();
		assertEquals(expectedPlayers.size(), turnOrder.size());
		assertEquals(expectedPlayers.get(0), turnOrder.get(0));
		assertEquals(expectedPlayers.get(1), turnOrder.get(1));
		assertEquals(expectedPlayers.get(2), turnOrder.get(2));

		EasyMock.verify(playerManagerWithThreePlayers);
	}

	private PlayerManager mockPlayerManager(int numPlayers) {
		PlayerManager playerManager = EasyMock.createMock(PlayerManager.class);
		List<Player> players = new ArrayList<>();
		for (int i = 0; i < numPlayers; i++) {
			players.add(mockPlayer());
		}
		EasyMock.expect(playerManager.getPlayers()).andStubReturn(players);
		EasyMock.replay(playerManager);
		return playerManager;
	}

	private List<Player> mockTwoPlayersList() {
		List<Player> players = new ArrayList<>();
		players.add(mockPlayer());
		players.add(mockPlayer());
		return players;
	}

	private Player mockPlayer() {
		return EasyMock.createMock(Player.class);
	}
}
