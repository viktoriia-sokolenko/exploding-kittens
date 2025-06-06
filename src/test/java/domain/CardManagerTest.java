package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class CardManagerTest {
	private CardManager cardManager;
	private Player player;
	private SkipCard skipCard;

	@BeforeEach
	void setUp() {
		cardManager = new CardManager();
		player	= EasyMock.createMock(Player.class);
		skipCard = EasyMock.createMock(SkipCard.class);
	}

	@Test
	void playCard_withNullCard_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			cardManager.playCard(null, player);
		});
	}

	@Test
	void playCard_withNullPlayer_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			cardManager.playCard(skipCard, null);
		});
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	void playCard_playerDoesNotHaveCard_throwsIllegalArgumentException(CardType testCardType) {
		Card mockCard = mockCard(testCardType);
		player.removeCardFromHand(mockCard);
		EasyMock.expectLastCall()
				.andThrow(new IllegalArgumentException
						("Card not in hand: can not remove card"));
		EasyMock.replay(player);
		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> cardManager.playCard(mockCard, player)
		);
		assertTrue(
				ex.getMessage().contains("Card not in hand: can not remove card"),
				"Expected exception message to mention missing card type"
		);

		EasyMock.verify(player);
	}

	//TODO: once all card effects are implemented
	// turn this test into parametrized test to test
	// whether playCard executes CardEffect for all card types
	// if player has that card in hand
	@Test
	void playCard_playerHasCard_executesCardEffect() {
		player.removeCardFromHand(skipCard);
		EasyMock.expectLastCall().once();
		CardEffect effectMock = EasyMock.createMock(CardEffect.class);
		EasyMock.expect(skipCard.createEffect()).andReturn(effectMock);
		effectMock.execute(EasyMock.anyObject(GameContext.class));
		EasyMock.expectLastCall().once();
		EasyMock.replay(skipCard, player, effectMock);
		assertDoesNotThrow(() -> cardManager.playCard(skipCard, player));
		EasyMock.verify(skipCard, player, effectMock);
	}

	private Card mockCard(CardType cardType) {
		Card mockCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
		EasyMock.replay(mockCard);
		return mockCard;
	}

}
