package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShuffleCardTest {
	@Test
	void createEffect_shuffleCard_returnsNonNullEffect() {
		ShuffleCard shuffleCard = new ShuffleCard();
		CardEffect effect = shuffleCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_shuffleEffect_callShuffleDeckFromDeck() {
		ShuffleCard shuffleCard = new ShuffleCard();
		CardEffect shuffleEffect = shuffleCard.createEffect();
		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.shuffleDeckFromDeck();
		EasyMock.expectLastCall();
		EasyMock.replay(mockGameContext);
		shuffleEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
