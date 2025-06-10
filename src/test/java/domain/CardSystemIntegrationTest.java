package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CardSystemIntegrationTest {

	private CardManager cardManager;
	private Player player;
	private Hand hand;
	private GameContext gameContext;

	@BeforeEach
	void setUp() {
		cardManager = new CardManager();
		hand = new Hand();
		player = new Player(hand);
		gameContext = new GameContext(player);
	}

	@Test
	void playSkipCard_integrationTest_removesCardFromHand() {
		SkipCard skipCard = new SkipCard();
		hand.addCard(skipCard);
		assertEquals(1, player.getCardTypeCount(CardType.SKIP));
		cardManager.playCard(skipCard, player, gameContext);
		assertEquals(0, player.getCardTypeCount(CardType.SKIP));
	}

	@Test
	void playSkipCard_playerDoesNotHaveCard_throwsIllegalArgumentException() {
		NormalCard normalCard = new NormalCard();
		hand.addCard(normalCard);
		SkipCard skipCard = new SkipCard();

		assertEquals(0, player.getCardTypeCount(CardType.SKIP));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> cardManager.playCard(skipCard, player, gameContext)
		);

		assertTrue(exception.getMessage()
				.contains("Card not in hand: can not remove card"));
	}

	@Test
	void playMultipleSkipCards_integrationTest_worksCorrectly() {
		SkipCard skipCard1 = new SkipCard();
		SkipCard skipCard2 = new SkipCard();

		hand.addCard(skipCard1);
		hand.addCard(skipCard2);
		assertEquals(2, player.getCardTypeCount(CardType.SKIP));
		cardManager.playCard(skipCard1, player, gameContext);
		assertEquals(1, player.getCardTypeCount(CardType.SKIP));
		cardManager.playCard(skipCard2, player, gameContext);
		assertEquals(0, player.getCardTypeCount(CardType.SKIP));
	}
}