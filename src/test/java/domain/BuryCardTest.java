package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuryCardTest {
	private BuryCard buryCard;

	@BeforeEach
	void setUp() {
		buryCard = new BuryCard();
	}

	@Test
	void constructor_createsCardWithBuryType() {
		assertEquals(CardType.BURY, buryCard.getCardType());
	}

	@Test
	void createEffect_buryCard_returnsNonNullEffect() {
		CardEffect effect = buryCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}


}
