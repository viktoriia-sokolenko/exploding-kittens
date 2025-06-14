package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FavorCardTest {
	@Test
	void createEffect_favorCard_returnsNonNullEffect() {
		FavorCard favorCard = new FavorCard();
		CardEffect effect = favorCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}
}
