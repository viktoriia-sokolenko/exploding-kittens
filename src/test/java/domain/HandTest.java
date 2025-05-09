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
		Card mockCard = EasyMock.mock(Card.class);
		Hand hand = new Hand();
		hand.addCard (mockCard);
		assertFalse(hand.isEmpty());
	}

	@Test
	public void isEmpty_withTwoCardsInHand_returnsFalse() {
		Card mockCard1 = EasyMock.mock(Card.class);
		Card mockCard2 = EasyMock.mock(Card.class);

		Hand hand = new Hand();
		hand.addCard (mockCard1);
		hand.addCard (mockCard2);
		assertFalse(hand.isEmpty());
	}

	@Test
	public void containsCardType_onEmptyHand_returnsFalse() {
		Hand hand = new Hand();
		CardType cardType = CardType.NUKE;
		assertFalse(hand.containsCardType(cardType));
	}

	@Test
	public void containsCardType_withCardInHand_returnsTrue() {
		Card card = EasyMock.mock(Card.class);
		CardType cardType = CardType.ATTACK;
		EasyMock.expect(card.getCardType()).andReturn(cardType);
		EasyMock.replay(card);

		Hand hand = new Hand();
		hand.addCard (card);

		assertTrue(hand.containsCardType(cardType));
		EasyMock.verify(card);
	}

}