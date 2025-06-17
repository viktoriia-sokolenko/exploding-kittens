package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class DefuseCardTest {
	@Test
	void createEffect_defuseCard_returnsNullEffect() {
		DefuseCard defuseCard = new DefuseCard();
		CardEffect effect = defuseCard.createEffect();

		assertNull(effect);
	}
}
