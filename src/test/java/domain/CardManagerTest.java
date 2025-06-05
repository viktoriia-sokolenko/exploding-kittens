package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

	@Test
	void playCard_playerDoesNotHaveCard_throwsIllegalArgumentException() {
		player.removeCardFromHand(skipCard);
		EasyMock.expectLastCall()
				.andThrow(new IllegalArgumentException
						("Card not in hand: can not remove card"));
		EasyMock.replay(player);
		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> cardManager.playCard(skipCard, player)
		);
		assertTrue(
				ex.getMessage().contains("Card not in hand: can not remove card"),
				"Expected exception message to mention missing card type"
		);

		EasyMock.verify(player);
	}

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

}
