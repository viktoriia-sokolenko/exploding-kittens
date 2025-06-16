package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SwapTopAndBottomCardTest {
	@Test
	void createEffect_swapTopAndBottomCard_returnsNonNullEffect() {
		SwapTopAndBottomCard swapTopAndBottomCard = new SwapTopAndBottomCard();
		CardEffect effect = swapTopAndBottomCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_SwapTopAndBottomEffect_callsSwapTopAndBottomDeckCards() {
		SwapTopAndBottomCard swapTopAndBottomCard = new SwapTopAndBottomCard();
		CardEffect favorEffect = swapTopAndBottomCard.createEffect();

		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.swapTopAndBottomDeckCards();
		EasyMock.expectLastCall();
		EasyMock.replay(mockGameContext);

		favorEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
