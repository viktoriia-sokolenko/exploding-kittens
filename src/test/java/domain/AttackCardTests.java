package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
