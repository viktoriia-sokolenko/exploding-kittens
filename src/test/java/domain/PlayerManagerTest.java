package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class PlayerManagerTest {
	private PlayerManager playerManager;
	private Deck mockDeck;

	@BeforeEach
	void setUp() {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			cards.add(new SkipCard()); // NOTE: adding skip cards because
			// that's the only that is semi-completed
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
	void addPlayers_withTooFewPlayers_throwsException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.addPlayers(1)
		);

		assertTrue(exception.getMessage().contains("Number of players must be between 2 and 5"));
	}

	@Test
	void addPlayers_withTooManyPlayers_throwsException() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> playerManager.addPlayers(6)
		);

		assertTrue(exception.getMessage().contains("Number of players must be between 2 and 5"));
	}

	@Test
	void addPlayers_withValidNumber_createsThatManyPlayers() {
		playerManager.addPlayers(3);

		assertEquals(3, playerManager.getPlayers().size());
		assertEquals(3, playerManager.getActivePlayers().size());

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
	void removePlayerFromGame_withValidPlayer_marksInactive() {
		playerManager.addPlayers(3);
		List<Player> players = playerManager.getPlayers();
		Player playerToRemove = players.get(0);

		assertTrue(playerToRemove.isInGame());
		assertEquals(3, playerManager.getActivePlayers().size());

		playerManager.removePlayerFromGame(playerToRemove);

		assertFalse(playerToRemove.isInGame());
		assertEquals(2, playerManager.getActivePlayers().size());
		assertEquals(3, playerManager.getPlayers().size());
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
		playerManager.addPlayers(4);
		List<Player> allPlayers = playerManager.getPlayers();

		playerManager.removePlayerFromGame(allPlayers.get(0));
		playerManager.removePlayerFromGame(allPlayers.get(2));

		List<Player> activePlayers = playerManager.getActivePlayers();

		assertEquals(2, activePlayers.size());
		assertEquals(4, allPlayers.size());
		for (Player player : activePlayers) {
			assertTrue(player.isInGame());
		}
	}

}
