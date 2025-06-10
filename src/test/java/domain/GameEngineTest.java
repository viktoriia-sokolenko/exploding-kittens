package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
	private GameEngine gameEngine;

	@BeforeEach
	void setUp() {
		gameEngine = new GameEngine();
	}

	@Test
	void playCard_withNullPlayer_throwsNullPointerException() {
		SkipCard skipCard = new SkipCard();

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.playCard(null, skipCard)
		);

		assertEquals("Player cannot be null", exception.getMessage());
	}

	@Test
	void playCard_withNullCard_throwsNullPointerException() {
		Hand hand = new Hand();
		Player player = new Player(hand);

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.playCard(player, null)
		);

		assertEquals("Card cannot be null", exception.getMessage());
	}

	@Test
	void playCard_playerHasCard_executesCardEffect() {
		Hand hand = new Hand();
		Player player = new Player(hand);
		SkipCard skipCard = new SkipCard();

		hand.addCard(skipCard);

		assertEquals(1, player.getCardTypeCount(CardType.SKIP));

		assertDoesNotThrow(() -> gameEngine.playCard(player, skipCard));
		assertEquals(0, player.getCardTypeCount(CardType.SKIP));
	}
}
