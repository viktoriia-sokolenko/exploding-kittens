package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class TurnManagerTest {

	private TurnManager turnManager;
	private Deck deck;
	private PlayerManager playerManager;
	private static final int NUM_CARDS = 20;
	private static final int DEFAULT_NUM_PLAYERS = 3;

	@BeforeEach
	void setUp() {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < NUM_CARDS; i++) {
			// Using SkipCard since it’s the only fully implemented card type
			// currently.
			cards.add(new SkipCard());
		}
		deck = new Deck(cards);
		turnManager = new TurnManager(deck);

		playerManager = new PlayerManager(deck);
		playerManager.addPlayers(DEFAULT_NUM_PLAYERS);
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

	@Test
	void getTurnsCountFor_nullPlayer_throwsNullPointerException() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> turnManager.getTurnsFor(null)
		);

		assertTrue(exception.getMessage().contains("Player cannot be null"));
	}

	@Test
	void getTurnsCountFor_emptyQueue_returnsZero() {
		Hand hand = new Hand();
		Player player = new Player(hand);

		final int EXPECTED_COUNT = 0;
		int actualCount = turnManager.getTurnsFor(player);
		assertEquals(EXPECTED_COUNT, actualCount);
	}

	@Test
	void getTurnsCountFor_playerInQueueWithTwo_returnsOne() {
		PlayerManager mockedPlayerManager = new PlayerManager(deck);
		mockedPlayerManager.addPlayers(2);
		turnManager.setPlayerManager(mockedPlayerManager);
		List<Player> players = mockedPlayerManager.getPlayers();
		Player player1 = players.get(0);

		final int EXPECTED_COUNT = 1;
		int actualCount = turnManager.getTurnsFor(player1);
		assertEquals(EXPECTED_COUNT, actualCount);
	}

	@Test
	void getTurnsCountFor_playerNotInQueueWithTwo_returnsZero() {
		Hand hand = new Hand();
		PlayerManager mockedPlayerManager = new PlayerManager(deck);
		mockedPlayerManager.addPlayers(2);
		turnManager.setPlayerManager(mockedPlayerManager);
		Player player3 = new Player(hand);

		final int EXPECTED_COUNT = 0;
		int actualCount = turnManager.getTurnsFor(player3);
		assertEquals(EXPECTED_COUNT, actualCount);
	}

	@Test
	void getTurnsCountFor_duplicatePlayerInQueueWithTwo_returnsTwo() {
		Hand hand = new Hand();
		PlayerManager mockedPlayerManager = new PlayerManager(deck);
		mockedPlayerManager.addPlayers(2);
		turnManager.setPlayerManager(mockedPlayerManager);
		Player player1 = mockedPlayerManager.getPlayers().get(0);

		assertEquals(player1, turnManager.getCurrentActivePlayer());
		turnManager.addTurnForCurrentPlayer();

		final int EXPECTED_COUNT = 2;
		int actualCount = turnManager.getTurnsFor(player1);
		assertEquals(EXPECTED_COUNT, actualCount);
	}

	@Test
	void getTurnsCountFor_playerInQueueWithFive_returnsOne() {
		playerManager.addPlayers(2);
		final int QUEUE_FIVE = 5;
		int queueSize = playerManager.getPlayers().size();
		assertEquals(QUEUE_FIVE, queueSize);

		final int INDEX_FOUR = 4;
		turnManager.setPlayerManager(playerManager);
		Player player5 = playerManager.getPlayers().get(INDEX_FOUR);

		final int EXPECTED_COUNT = 1;
		int actualCount = turnManager.getTurnsFor(player5);
		assertEquals(EXPECTED_COUNT, actualCount);
	}

}
