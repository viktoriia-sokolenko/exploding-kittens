package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AttackCardTests {
	private AttackCard attackCard;

	@BeforeEach
	void setUp() {
		attackCard = new AttackCard();
	}

	@Test
	void constructor_createsCardWithAttackType() {
		assertEquals(CardType.ATTACK, attackCard.getCardType());
	}

	@Test
	void createEffect_returnsNonNullEffect() {
		CardEffect effect = attackCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}
}
