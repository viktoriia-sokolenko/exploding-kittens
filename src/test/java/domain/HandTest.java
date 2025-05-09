package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HandTest {
	@Test
	public void isEmpty_onEmptyHand_returnsTrue() {
		Hand hand = new Hand();
		assertTrue(hand.isEmpty());
	}

	@Test
	public void isEmpty_withOneCardInHand_returnsFalse() {
		Hand hand = new Hand();
		Card mockCard = EasyMock.mock(Card.class);
		hand.addCard (mockCard);
		assertFalse(hand.isEmpty());
	}

	@Test
	public void isEmpty_withTwoCardsInHand_returnsFalse() {
		Hand hand = new Hand();
		Card mockCard1 = EasyMock.mock(Card.class);
		Card mockCard2 = EasyMock.mock(Card.class);
		hand.addCard (mockCard1);
		hand.addCard (mockCard2);
		assertFalse(hand.isEmpty());
	}

}