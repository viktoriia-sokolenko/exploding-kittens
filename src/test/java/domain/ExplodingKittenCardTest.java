package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ExplodingKittenCardTest {
	@Test
	void createEffect_explodingKittenCard_returnsNullEffect() {
		ExpoldingKittenCard explodingKittenCard = new ExpoldingKittenCard();
		CardEffect effect = explodingKittenCard.createEffect();

		assertNull(effect);
	}
}
