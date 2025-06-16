package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SwapTopAndBottomCardTest {
	@Test
	void createEffect_swapTopAndBottomCard_returnsNonNullEffect() {
		SwapTopAndBottomCard swapTopAndBottomCard = new SwapTopAndBottomCard();
		CardEffect effect = swapTopAndBottomCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}
}
