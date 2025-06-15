package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReverseCardTest {
	@Test
	void createEffect_reverseCard_returnsNonNullEffect() {
		ReverseCard reverseCard = new ReverseCard();
		CardEffect effect = reverseCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}
}
