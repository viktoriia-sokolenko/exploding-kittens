package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlterTheFutureTest {

	@Test
	void createEffect_alterTheFutureCard_returnsNonNullEffect() {
		AlterTheFutureCard alterTheFutureCard = new AlterTheFutureCard();
		CardEffect effect = alterTheFutureCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}
}
