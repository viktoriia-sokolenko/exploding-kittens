package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class PlayerManagerTest {
	private static final int NUM_CARDS = 20;
	private static final int TOO_MANY_PLAYERS = 6;
	private static final int DEFAULT_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;

	private PlayerManager playerManager;
	private Deck mockDeck;

	@BeforeEach
	void setUp() {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < NUM_CARDS; i++) {
			cards.add(new SkipCard());
			cards.add(new AttackCard());
		}
		mockDeck = new Deck(cards);
		playerManager = new PlayerManager(mockDeck);
	}

	@Test
	void constructor_withNullDeck_throwsNullPointerException() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> new PlayerManager(null)
		);

		assertEquals("Deck cannot be null", exception.getMessage());
	}

	@Test
	void constructor_withValidDeck_initializesEmptyManager() {
		PlayerManager pm = new PlayerManager(mockDeck);

		assertEquals(0, pm.getPlayers().size());
		assertEquals(0, pm.getActivePlayers().size());
	}

	@Test
	void addPlayers_withTooFewPlayers_throwsIllegalArgumentException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.addPlayers(1)
		);

		assertTrue(exception.getMessage()
				.contains("Number of players must be between 2 and 5"));
	}

	@Test
	void addPlayers_withTooManyPlayers_throwsIllegalArgumentException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.addPlayers(TOO_MANY_PLAYERS)
		);

		assertTrue(exception.getMessage()
				.contains("Number of players must be between 2 and 5"));
	}

	@Test
	void addPlayers_withValidNumber_createsThatManyPlayers() {
		playerManager.addPlayers(DEFAULT_PLAYERS);

		assertEquals(DEFAULT_PLAYERS, playerManager.getPlayers().size());
		assertEquals(DEFAULT_PLAYERS, playerManager.getActivePlayers().size());

		for (Player player : playerManager.getPlayers()) {
			assertTrue(player.isInGame());
		}
	}

	@Test
	void addPlayers_withTwoPlayers_initializesBothActive() {
		playerManager.addPlayers(2);

		List<Player> players = playerManager.getPlayers();
		assertEquals(2, players.size());
		for (Player player : players) {
			assertEquals(0, player.getCardTypeCount(CardType.SKIP));
			assertTrue(player.isInGame());
		}
	}

	@Test
	void removePlayerFromGame_withValidPlayer_marksPlayerInactive() {
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
	void removePlayerFromGame_withNullPlayer_throwsNullPointerException() {
		playerManager.addPlayers(2);

		assertThrows(NullPointerException.class, () -> {
			playerManager.removePlayerFromGame(null);
		});
	}

	@Test
	void removePlayerFromGame_withPlayerNotInGame_throwsIllegalArgumentException() {
		playerManager.addPlayers(2);
		Hand otherHand = new Hand();
		Player otherPlayer = new Player(otherHand);

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.removePlayerFromGame(otherPlayer)
		);

		assertTrue(exception.getMessage().contains("Player not found"));
	}

	@Test
	void getActivePlayers_afterRemovals_returnsOnlyActivePlayers() {
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
}
