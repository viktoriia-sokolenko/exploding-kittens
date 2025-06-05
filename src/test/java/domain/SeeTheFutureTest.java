package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SeeTheFutureTest {
	@Test
	void createEffect_SeeTheFutureCard_returnsNonNullEffect() {
		SeeTheFutureCard seeTheFutureCard = new SeeTheFutureCard();
		CardEffect effect = seeTheFutureCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}
}
