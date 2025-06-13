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
	private GameContext gameContext;

	@BeforeEach
	void setUp() {
		cardManager = new CardManager();
		player	= EasyMock.createMock(Player.class);
		skipCard = EasyMock.createMock(SkipCard.class);
		EasyMock.expect(skipCard.getCardType()).andStubReturn(CardType.SKIP);
		gameContext = EasyMock.createMock(GameContext.class);
	}

	@Test
	void playCard_withNullCard_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			cardManager.playCard(null, player, gameContext);
		});
	}

	@Test
	void playCard_withNullPlayer_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			cardManager.playCard(skipCard, null, gameContext);
		});
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	void playCard_playerDoesNotHaveCard_throwsIllegalArgumentException(CardType testCardType) {
		Card mockCard = mockCard(testCardType);
		player.removeCardFromHand(testCardType);
		EasyMock.expectLastCall()
				.andThrow(new IllegalArgumentException
						("Card not in hand: can not remove card"));
		EasyMock.replay(player);
		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> cardManager.playCard(mockCard, player, gameContext)
		);
		assertTrue(
				ex.getMessage().contains("Card not in hand: can not remove card"),
				"Expected exception message to mention missing card type"
		);

		EasyMock.verify(player);
	}

	@Test
	void playCard_playerHasCard_executesCardEffect() {
		player.removeCardFromHand(CardType.SKIP);
		EasyMock.expectLastCall().once();
		CardEffect effectMock = EasyMock.createMock(CardEffect.class);
		EasyMock.expect(skipCard.createEffect()).andReturn(effectMock);
		effectMock.execute(EasyMock.anyObject(GameContext.class));
		EasyMock.expectLastCall().once();
		EasyMock.replay(skipCard, player, effectMock);

		assertDoesNotThrow(() -> cardManager.playCard(skipCard, player, gameContext));
		EasyMock.verify(skipCard, player, effectMock);
	}

	private Card mockCard(CardType cardType) {
		Card mockCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
		EasyMock.replay(mockCard);
		return mockCard;
	}

}
