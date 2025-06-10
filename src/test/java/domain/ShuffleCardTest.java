package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShuffleCardTest {
	@Test
	void createEffect_shuffleCard_returnsNonNullEffect() {
		ShuffleCard shuffleCard = new ShuffleCard();
		CardEffect effect = shuffleCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}
}
