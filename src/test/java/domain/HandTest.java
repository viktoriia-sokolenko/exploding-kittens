package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

	private Hand handWithOneCard(){
		Card mockCard = EasyMock.mock(Card.class);
		CardType cardType = CardType.ATTACK;
		EasyMock.expect(mockCard.getCardType()).andReturn(cardType);
		EasyMock.replay(mockCard);

		Hand hand = new Hand();
		hand.addCard(mockCard);

		EasyMock.verify(mockCard);

		return hand;
	}
	private Hand handWithTwoCards(){
		Card mockCard1 = EasyMock.mock(Card.class);
		CardType cardType1 = CardType.SEE_THE_FUTURE;
		EasyMock.expect(mockCard1.getCardType()).andReturn(cardType1);

		Card mockCard2 = EasyMock.mock(Card.class);
		CardType cardType2 = CardType.SHUFFLE;
		EasyMock.expect(mockCard2.getCardType()).andReturn(cardType2);

		EasyMock.replay(mockCard1, mockCard2);

		Hand hand = new Hand();
		hand.addCard(mockCard1);
		hand.addCard(mockCard2);

		EasyMock.verify(mockCard1, mockCard2);

		return hand;
	}

	private Hand handWithThreeCardsAndDuplicates(){
		Card extraCard = EasyMock.mock(Card.class);
		CardType extraCardType2 = CardType.SKIP;
		EasyMock.expect(extraCard.getCardType()).andReturn(extraCardType2);

		CardType duplicateCardType = CardType.NORMAL;
		Card duplicateCard1 = EasyMock.mock(Card.class);
		EasyMock.expect(duplicateCard1.getCardType()).andReturn(duplicateCardType);

		Card duplicateCard2 = EasyMock.mock(Card.class);
		EasyMock.expect(duplicateCard2.getCardType()).andReturn(duplicateCardType);

		EasyMock.replay(extraCard, duplicateCard1, duplicateCard2);

		Hand hand = new Hand();
		hand.addCard(extraCard);
		hand.addCard(duplicateCard1);
		hand.addCard(duplicateCard2);

		EasyMock.verify(extraCard, duplicateCard1, duplicateCard2);

		return hand;
	}

	@Test
	public void isEmpty_onEmptyHand_returnsTrue() {
		Hand emptyHand = new Hand();
		assertTrue(emptyHand.isEmpty());
	}

	@Test
	public void isEmpty_withOneCardInHand_returnsFalse() {
		Hand handWithOneCard = handWithOneCard();
		assertFalse(handWithOneCard.isEmpty());
	}

	@Test
	public void isEmpty_withTwoCardsInHand_returnsFalse() {
		Hand hand = handWithTwoCards();
		assertFalse(hand.isEmpty());
	}

	@Test
	public void containsCardType_onEmptyHand_returnsFalse() {
		Hand emptyHand = new Hand();
		CardType cardType = CardType.NUKE;
		assertFalse(emptyHand.containsCardType(cardType));
	}

	@Test
	public void containsCardType_withCardInHand_returnsTrue() {
		Hand handWithOneCard = handWithOneCard();
		CardType cardType = CardType.ATTACK;
		assertTrue(handWithOneCard.containsCardType(cardType));
	}

	@Test
	public void containsCardType_withTwoOtherCardsInHand_returnsFalse() {
		Hand hand = handWithTwoCards();
		CardType expectedCardType = CardType.DEFUSE;
		assertFalse(hand.containsCardType(expectedCardType));
	}

	@Test
	public void containsCardType_withDuplicatesInHand_returnsTrue() {
		Hand hand = handWithThreeCardsAndDuplicates();
		CardType expectedCardType = CardType.NORMAL;
		assertTrue(hand.containsCardType(expectedCardType));
	}

	@Test
	public void getNumberOfCards_onEmptyHand_returnsZero() {
		Hand emptyHand = new Hand();
		int expectedNumberOfCards = 0;
		assertEquals(expectedNumberOfCards, emptyHand.getNumberOfCards());
	}

	@Test
	public void getNumberOfCards_withOneCardInHand_returnsOne() {
		Hand handWithOneCard = handWithOneCard();
		int expectedNumberOfCards = 1;
		assertEquals(expectedNumberOfCards, handWithOneCard.getNumberOfCards());
	}

	@Test
	public void getNumberOfCards_withTwoCardsInHand_returnsTwo() {
		Hand handWithTwoCards = handWithTwoCards();
		int expectedNumberOfCards = 2;
		assertEquals(expectedNumberOfCards, handWithTwoCards.getNumberOfCards());
	}

}