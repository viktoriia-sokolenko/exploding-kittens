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
	void endTurnWithoutDrawForAttacks_emptyQueue_throwsIllegalStateException() {
		assertThrows(IllegalStateException.class, () -> {
			turnManager.endTurnWithoutDrawForAttacks();
		});
	}

	@Test
	void endTurnWithoutDrawForAttacks_withTwoPlayers_incrementTurnForPlayerTwo() {
		PlayerManager playerManagerWithTwoPlayers = mockPlayerManager(2);
		turnManager.setPlayerManager(playerManagerWithTwoPlayers);

		List<Player> players = playerManagerWithTwoPlayers.getPlayers();
		Player firstPlayer = players.get(0);
		Player secondPlayer = players.get(1);

		final int TURN_THREE = 3;
		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());
		turnManager.endTurnWithoutDrawForAttacks();
		assertEquals(secondPlayer, turnManager.getCurrentActivePlayer());
		assertEquals(TURN_THREE, turnManager.getTurnsFor(secondPlayer));

		EasyMock.verify(playerManagerWithTwoPlayers);
	}

	@Test
	void endTurnWithoutDrawForAttacks_withThreePlayers_incrementTurnForPlayerThree() {
		final int PLAYERS_THREE = 3;
		PlayerManager playerManagerWithThreePlayers = mockPlayerManager(PLAYERS_THREE);
		turnManager.setPlayerManager(playerManagerWithThreePlayers);

		List<Player> players = playerManagerWithThreePlayers.getPlayers();
		Player firstPlayer = players.get(0);
		Player secondPlayer = players.get(1);
		Player thirdPlayer = players.get(2);

		final int TURN_THREE = 3;
		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());
		turnManager.endTurnWithoutDraw();
		assertEquals(secondPlayer, turnManager.getCurrentActivePlayer());
		turnManager.endTurnWithoutDrawForAttacks();
		assertEquals(thirdPlayer, turnManager.getCurrentActivePlayer());
		assertEquals(TURN_THREE, turnManager.getTurnsFor(thirdPlayer));

		EasyMock.verify(playerManagerWithThreePlayers);
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

	@Test
	void getTurnsCountFor_nullPlayer_throwsNullPointerException() {
		NullPointerException exception = assertThrows(NullPointerException.class,
				() -> turnManager.getTurnsFor(null));

		assertTrue(exception.getMessage().contains("Player cannot be null"));
	}

	@Test
	void getTurnsCountFor_emptyQueue_returnsZero() {
		Player player = mockPlayer();

		final int EXPECTED_COUNT = 0;
		int actualCount = turnManager.getTurnsFor(player);
		assertEquals(EXPECTED_COUNT, actualCount);
	}

	@Test
	void getTurnsCountFor_playerInQueueWithTwo_returnsOne() {
		PlayerManager playerManagerWithTwoPlayers = mockPlayerManager(2);
		turnManager.setPlayerManager(playerManagerWithTwoPlayers);

		List<Player> players = playerManagerWithTwoPlayers.getPlayers();
		Player firstPlayer = players.get(0);

		final int EXPECTED_COUNT = 1;
		int actualCount = turnManager.getTurnsFor(firstPlayer);
		assertEquals(EXPECTED_COUNT, actualCount);

		EasyMock.verify(playerManagerWithTwoPlayers);
	}

	@Test
	void getTurnsCountFor_playerNotInQueueWithTwo_returnsZero() {
		PlayerManager playerManagerWithTwoPlayers = mockPlayerManager(2);
		turnManager.setPlayerManager(playerManagerWithTwoPlayers);
		Player thirdPlayer = mockPlayer();

		final int EXPECTED_COUNT = 0;
		int actualCount = turnManager.getTurnsFor(thirdPlayer);
		assertEquals(EXPECTED_COUNT, actualCount);

		EasyMock.verify(playerManagerWithTwoPlayers);
	}

	@Test
	void getTurnsCountFor_duplicatePlayerInQueueWithTwo_returnsTwo() {
		PlayerManager playerManagerWithTwoPlayers = mockPlayerManager(2);
		turnManager.setPlayerManager(playerManagerWithTwoPlayers);
		Player firstPlayer = playerManagerWithTwoPlayers.getPlayers().get(0);

		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());
		turnManager.addTurnForCurrentPlayer();

		final int EXPECTED_COUNT = 2;
		int actualCount = turnManager.getTurnsFor(firstPlayer);
		assertEquals(EXPECTED_COUNT, actualCount);
		EasyMock.verify(playerManagerWithTwoPlayers);
	}

	@Test
	void getTurnsCountFor_playerInQueueWithFive_returnsOne() {
		final int PLAYERS_FIVE = 5;
		PlayerManager playerManagerWithFivePlayers = mockPlayerManager(PLAYERS_FIVE);
		final int QUEUE_FIVE = 5;
		int queueSize = playerManagerWithFivePlayers.getPlayers().size();
		assertEquals(QUEUE_FIVE, queueSize);

		final int INDEX_FOUR = 4;
		turnManager.setPlayerManager(playerManagerWithFivePlayers);
		Player fifthPlayer = playerManagerWithFivePlayers.getPlayers().get(INDEX_FOUR);

		final int EXPECTED_COUNT = 1;
		int actualCount = turnManager.getTurnsFor(fifthPlayer);
		assertEquals(EXPECTED_COUNT, actualCount);
		EasyMock.verify(playerManagerWithFivePlayers);
	}

	@Test
	void addTurnForCurrentPlayer_withOnePlayer_doesNotAddDuplicateTurn() {
		PlayerManager playerManager = mockPlayerManager(1);
		turnManager.setPlayerManager(playerManager);
		final int ONLY_PLAYER = 0;
		Player singlePlayer = playerManager.getPlayers().get(ONLY_PLAYER);
		assertEquals(singlePlayer, turnManager.getCurrentActivePlayer());
		final int EXPECTED_COUNT = 1;
		assertEquals(EXPECTED_COUNT, turnManager.getTurnsFor(singlePlayer));
		turnManager.addTurnForCurrentPlayer();
		assertEquals(EXPECTED_COUNT, turnManager.getTurnsFor(singlePlayer));
		assertEquals(singlePlayer, turnManager.getCurrentActivePlayer());
		turnManager.endTurnWithoutDraw();
		assertEquals(singlePlayer, turnManager.getCurrentActivePlayer());

		EasyMock.verify(playerManager);
	}

	@Test
	void reverseOrder_emptyQueue_throwsIllegalStateException() {
		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> turnManager.reverseOrder()
		);
		assertTrue(exception.getMessage().contains("No players to manage"));
	}

	@Test
	void reverseOrder_withTwoPlayers_orderReverses() {
		PlayerManager playerManagerWithTwoPlayers = mockPlayerManager(2);
		turnManager.setPlayerManager(playerManagerWithTwoPlayers);
		List<Player> players = playerManagerWithTwoPlayers.getPlayers();
		Player firstPlayer = players.get(0);
		Player secondPlayer = players.get(1);
		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());
		turnManager.reverseOrder();
		assertEquals(secondPlayer, turnManager.getCurrentActivePlayer());
		EasyMock.verify(playerManagerWithTwoPlayers);
	}

	@Test
	void reverseOrder_withThreePlayers_orderReverses() {
		PlayerManager playerManagerWithThreePlayers = mockPlayerManager(3);
		turnManager.setPlayerManager(playerManagerWithThreePlayers);
		List<Player> players = playerManagerWithThreePlayers.getPlayers();
		Player firstPlayer = players.get(0);
		Player thirdPlayer = players.get(2);
		assertEquals(firstPlayer, turnManager.getCurrentActivePlayer());
		turnManager.reverseOrder();
		assertEquals(thirdPlayer, turnManager.getCurrentActivePlayer());
		EasyMock.verify(playerManagerWithThreePlayers);
	}

	@Test
	public void isUnderAttack_defaultTurn_returnsFalse() {
		turnManager.setRequiredTurns(1);
		turnManager.setCurrentPlayerTurnsTaken(0);
		boolean result = turnManager.isUnderAttack();
		assertFalse(result);
	}

	@Test
	public void isUnderAttack_requiredTwoTakenZero_returnsTrue() {
		turnManager.setRequiredTurns(2);
		turnManager.setCurrentPlayerTurnsTaken(0);
		boolean result = turnManager.isUnderAttack();
		assertTrue(result);
	}

	@Test
	public void isUnderAttack_requiredTwoTakenOne_returnsTrue() {
		turnManager.setRequiredTurns(2);
		turnManager.setCurrentPlayerTurnsTaken(1);
		boolean result = turnManager.isUnderAttack();
		assertTrue(result);
	}

	@Test
	public void isUnderAttack_requiredTwoTakenTwo_returnsFalse() {
		turnManager.setRequiredTurns(2);
		turnManager.setCurrentPlayerTurnsTaken(2);
		boolean result = turnManager.isUnderAttack();
		assertFalse(result);
	}

	@Test
	public void isUnderAttack_requiredThreeTakenThree_returnsFalse() {
		turnManager.setRequiredTurns(3);
		turnManager.setCurrentPlayerTurnsTaken(3);
		boolean result = turnManager.isUnderAttack();
		assertFalse(result);
	}

	@Test
	public void isUnderAttack_requiredThreeTakenOne_returnsTrue() {
		turnManager.setRequiredTurns(3);
		turnManager.setCurrentPlayerTurnsTaken(1);
		boolean result = turnManager.isUnderAttack();
		assertTrue(result);
	}

	@Test
	public void incrementTurnsTaken_defaultTurn_advancesAndResets() {
		turnManager.setRequiredTurns(1);
		turnManager.setCurrentPlayerTurnsTaken(0);
		turnManager.incrementTurnsTaken();

		assertEquals(1, turnManager.getRequiredTurns());
		assertEquals(0, turnManager.getCurrentPlayerTurnsTaken());
	}

	@Test
	public void incrementTurnsTaken_partialAttackTurn_doesNotAdvance() {
		turnManager.setRequiredTurns(2);
		turnManager.setCurrentPlayerTurnsTaken(0);
		turnManager.incrementTurnsTaken();

		assertEquals(1, turnManager.getCurrentPlayerTurnsTaken());
		assertEquals(2, turnManager.getRequiredTurns());
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
