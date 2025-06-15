package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PlayerManagerTest {
	private static final int NUM_CARDS = 20;
	private static final int TOO_MANY_PLAYERS = 6;
	private static final int DEFAULT_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;

	private PlayerManager playerManager;
	private Deck mockDeck;

	@BeforeEach
	public void setUp() {
		mockDeck = mockDeck();
		playerManager = new PlayerManager(mockDeck);
	}

	@Test
	public void constructor_withNullDeck_throwsNullPointerException() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> new PlayerManager(null)
		);

		assertEquals("Deck cannot be null", exception.getMessage());
	}

	@Test
	public void constructor_withValidDeck_initializesEmptyManager() {
		PlayerManager pm = new PlayerManager(mockDeck);

		assertEquals(0, pm.getPlayers().size());
		assertEquals(0, pm.getActivePlayers().size());
	}

	@Test
	public void addPlayers_withTooFewPlayers_throwsIllegalArgumentException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.addPlayers(1)
		);

		assertTrue(exception.getMessage()
				.contains("Number of players must be between 2 and 5"));
	}

	@Test
	public void addPlayers_withTooManyPlayers_throwsIllegalArgumentException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.addPlayers(TOO_MANY_PLAYERS)
		);

		assertTrue(exception.getMessage()
				.contains("Number of players must be between 2 and 5"));
	}

	@Test
	public void addPlayers_withValidNumber_createsThatManyPlayers() {
		playerManager.addPlayers(DEFAULT_PLAYERS);

		assertEquals(DEFAULT_PLAYERS, playerManager.getPlayers().size());
		assertEquals(DEFAULT_PLAYERS, playerManager.getActivePlayers().size());

		for (Player player : playerManager.getPlayers()) {
			assertEquals(0, player.getNumberOfCards());
			assertTrue(player.isInGame());
		}
	}

	@Test
	public void addPlayers_withTwoPlayers_initializesBothActive() {
		playerManager.addPlayers(2);

		List<Player> players = playerManager.getPlayers();
		assertEquals(2, players.size());
		for (Player player : players) {
			assertEquals(0, player.getNumberOfCards());
			assertTrue(player.isInGame());
		}
	}

	@Test
	public void removePlayerFromGame_withValidPlayer_marksPlayerInactive() {
		playerManager.addPlayers(DEFAULT_PLAYERS);
		List<Player> players = playerManager.getPlayers();
		Player playerToRemove = players.get(0);

		assertTrue(playerToRemove.isInGame());
		assertEquals(DEFAULT_PLAYERS, playerManager.getActivePlayers().size());

		playerManager.removePlayerFromGame(playerToRemove);

		assertFalse(playerToRemove.isInGame());
		assertEquals(2, playerManager.getActivePlayers().size());
		assertEquals(DEFAULT_PLAYERS, playerManager.getPlayers().size());
	}

	@Test
	public void removePlayerFromGame_withNullPlayer_throwsNullPointerException() {
		playerManager.addPlayers(2);

		assertThrows(NullPointerException.class, () -> {
			playerManager.removePlayerFromGame(null);
		});
	}

	@Test
	public void removePlayerFromGame_withPlayerNotInGame_throwsIllegalArgumentException() {
		playerManager.addPlayers(2);
		Player otherPlayer = mockPlayer();

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.removePlayerFromGame(otherPlayer)
		);

		assertTrue(exception.getMessage().contains("Player not found"));
	}

	@Test
	public void getActivePlayers_afterRemovals_returnsOnlyActivePlayers() {
		playerManager.addPlayers(FOUR_PLAYERS);
		List<Player> allPlayers = playerManager.getPlayers();

		playerManager.removePlayerFromGame(allPlayers.get(0));
		playerManager.removePlayerFromGame(allPlayers.get(2));

		List<Player> activePlayers = playerManager.getActivePlayers();

		assertEquals(2, activePlayers.size());
		assertEquals(FOUR_PLAYERS, allPlayers.size());
		for (Player player : activePlayers) {
			assertTrue(player.isInGame());
		}
	}

	@Test
	public void getPlayerByIndex_withNegativeIndex_throwsIndexOutOfBoundsException() {
		playerManager.addPlayers(2);
		assertThrows(IndexOutOfBoundsException.class,
				() -> playerManager.getPlayerByIndex(-1));
	}

	@Test
	public void getPlayerByIndex_noPlayersWithPositiveIndex_throwsIndexOutOfBoundsException() {
		assertThrows(IndexOutOfBoundsException.class,
				() -> playerManager.getPlayerByIndex(1));
	}

	@Test
	public void getPlayerByIndex_twoPlayersWithTwoIndex_throwsIndexOutOfBoundsException() {
		playerManager.addPlayers(2);
		assertThrows(IndexOutOfBoundsException.class,
				() -> playerManager.getPlayerByIndex(2));
	}

	@Test
	public void getPlayerByIndex_twoPlayersWithZeroIndex_returnsFirstPlayer() {
		playerManager.addPlayers(2);
		List <Player> allPlayers = playerManager.getPlayers();
		assertDoesNotThrow(() -> {
			Player expectedPlayer = allPlayers.get(0);
			Player actualPlayer = playerManager.getPlayerByIndex(0);
			assertEquals(expectedPlayer, actualPlayer);
		});
	}

	@Test
	public void getPlayerByIndex_twoPlayersWithOneIndex_returnsSecondPlayer() {
		playerManager.addPlayers(2);
		List <Player> allPlayers = playerManager.getPlayers();
		assertDoesNotThrow(() -> {
			Player expectedPlayer = allPlayers.get(1);
			Player actualPlayer = playerManager.getPlayerByIndex(1);
			assertEquals(expectedPlayer, actualPlayer);
		});
	}

	@Test
	public void getPlayerByIndex_twoPlayersWithThreeIndex_throwsIndexOutOfBoundsException() {
		playerManager.addPlayers(2);
		int sizePlusOneIndex = playerManager.getPlayers().size() + 1;
		assertThrows(IndexOutOfBoundsException.class,
				() -> playerManager.getPlayerByIndex(sizePlusOneIndex));
	}

	private Deck mockDeck() {
		return EasyMock.createMock(Deck.class);
	}

	private Player mockPlayer() {
		return EasyMock.createMock(Player.class);
	}
}
