package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class NormalCardTest {
	@Test
	void createEffect_normalCard_returnsNullEffect() {
		NormalCard normalCard = new NormalCard();
		CardEffect effect = normalCard.createEffect();

		assertNull(effect);
	}
}
