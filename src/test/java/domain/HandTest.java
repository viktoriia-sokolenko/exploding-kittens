package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

	@Test
	public void isEmpty_withEmptyHand_returnsTrue() {
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
	public void containsCardType_withNullCardType_throwsNullPointerException() {
		Hand hand = handWithOneCard();
		assertThrows(NullPointerException.class, () -> hand.containsCardType(null));
	}

	@Test
	public void containsCardType_withEmptyHand_returnsFalse() {
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
	public void getNumberOfCards_withEmptyHand_returnsZero() {
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

	@Test
	public void getNumberOfCards_withThreeCardsInHandAndDuplicates_returnsThree() {
		Hand handWithThreeCardsAndDuplicates = handWithThreeCardsAndDuplicates();
		final int expectedNumberOfCards = 3;
		assertEquals(expectedNumberOfCards,
				handWithThreeCardsAndDuplicates.getNumberOfCards());
	}

	@Test
	public void addCard_withNullCard_throwsNullPointerException() {
		Hand hand = handWithOneCard();
		assertThrows(NullPointerException.class, () -> hand.addCard(null));
	}

	@Test
	public void addCard_toEmptyHand_insertsCard() {
		CardType cardType = CardType.FAVOR;
		Card card = mockCard(cardType);

		Hand hand = new Hand();
		hand.addCard(card);

		int expectedNumberOfCards = 1;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());

		assertTrue(hand.containsCardType(cardType));
	}

	@Test
	public void addCard_toHandWithOneCard_insertsCard() {
		CardType cardType = CardType.SKIP;
		Card card = mockCard(cardType);

		Hand hand = handWithOneCard();
		hand.addCard(card);

		int expectedNumberOfCards = 2;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());

		assertTrue(hand.containsCardType(cardType));
	}

	@Test
	public void addCard_toHandWithSameCard_insertsDuplicateCard() {
		CardType cardType = CardType.SEE_THE_FUTURE;
		Card card = mockCard(cardType);

		Hand hand = handWithTwoCards();
		hand.addCard(card);

		final int expectedNumberOfCards = 3;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());

		assertTrue(hand.containsCardType(cardType));
	}

	@Test
	public void removeCard_withNullCard_throwsNullPointerException() {
		Hand hand = handWithOneCard();
		assertThrows(NullPointerException.class, () -> hand.removeCard(null));
	}

	@Test
	public void removeCard_withEmptyHand_throwsIllegalStateException() {
		Hand emptyHand = new Hand();
		Card card = mockCard(CardType.FAVOR);
		assertThrows(IllegalStateException.class, () -> emptyHand.removeCard(card));
	}

	@Test
	public void removeCard_withCardNotInHand_throwsIllegalArgumentException() {
		Hand hand = handWithTwoCards();
		Card card = mockCard(CardType.DEFUSE);
		assertThrows(IllegalArgumentException.class, () -> hand.removeCard(card));
	}

	@Test
	public void removeCard_withOneCardInHand_emptiesHand() {
		CardType cardType = CardType.ATTACK;
		Card card = mockCard(cardType);

		Hand hand = handWithOneCard();
		hand.removeCard(card);

		assertTrue(hand.isEmpty());
		assertFalse(hand.containsCardType(cardType));
	}

	@Test
	public void removeCard_withTwoCardsInHand_removesCard() {
		CardType cardType = CardType.SEE_THE_FUTURE;
		Card card = mockCard(cardType);

		Hand hand = handWithTwoCards();
		hand.removeCard(card);

		int expectedNumberOfCards = 1;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());

		assertFalse(hand.containsCardType(cardType));
	}

	@Test
	public void removeCard_withDuplicateCardsInHand_removesOnlyOneCard() {
		CardType cardType = CardType.NORMAL;
		Card card = mockCard(cardType);

		Hand hand = handWithThreeCardsAndDuplicates();
		hand.removeCard(card);

		int expectedNumberOfCards = 2;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());

		assertTrue(hand.containsCardType(cardType));
	}

	@Test
	public void getCountOfCardType_withNullCardType_throwsNullPointerException() {
		Hand hand = handWithOneCard();
		assertThrows(NullPointerException.class, () -> hand.getCountOfCardType(null));
	}

	@Test
	public void getCountOfCardType_withEmptyHand_returnsZero() {
		Hand emptyHand = new Hand();
		CardType cardType = CardType.SKIP;

		int expectedCount = 0;
		assertEquals(expectedCount, emptyHand.getCountOfCardType(cardType));
	}

	@Test
	public void getCountOfCardType_withCardInHand_returnsOne() {
		Hand hand = handWithOneCard();
		CardType cardType = CardType.ATTACK;
		int expectedCount = 1;
		assertEquals(expectedCount, hand.getCountOfCardType(cardType));
	}

	@Test
	public void getCountOfCardType_withCardNotInHand_returnsZero() {
		Hand hand = handWithTwoCards();
		CardType cardType = CardType.DEFUSE;
		int expectedCount = 0;
		assertEquals(expectedCount, hand.getCountOfCardType(cardType));
	}

	@Test
	public void getCountOfCardType_withTwoDuplicateCardsInHand_returnsTwo() {
		CardType duplicateCardType = CardType.NORMAL;
		Hand hand = handWithThreeCardsAndDuplicates();
		int expectedCount = 2;
		assertEquals(expectedCount, hand.getCountOfCardType(duplicateCardType));
	}

	@Test
	public void getCountOfCardType_withThreeDuplicateCardsInHand_returnsThree() {
		CardType duplicateCardType = CardType.FAVOR;
		Hand hand = handWithFiveCardsAndThreeDuplicates();
		final int expectedCount = 3;
		assertEquals(expectedCount, hand.getCountOfCardType(duplicateCardType));
	}

	private Card mockCard(CardType type) {
		Card card = EasyMock.createMock(Card.class);
		EasyMock.expect(card.getCardType()).andStubReturn(type);
		EasyMock.replay(card);
		return card;
	}

	private Hand handWithOneCard() {
		Card mockCard = mockCard(CardType.ATTACK);

		Hand hand = new Hand();
		hand.addCard(mockCard);

		return hand;
	}

	private Hand handWithTwoCards() {
		Card mockCard1 = mockCard(CardType.SEE_THE_FUTURE);
		Card mockCard2 = mockCard(CardType.SHUFFLE);

		Hand hand = new Hand();
		hand.addCard(mockCard1);
		hand.addCard(mockCard2);

		return hand;
	}

	private Hand handWithThreeCardsAndDuplicates() {
		Card extraCard = mockCard(CardType.SKIP);

		CardType duplicateCardType = CardType.NORMAL;
		Card duplicateCard1 = mockCard(duplicateCardType);
		Card duplicateCard2 = mockCard(duplicateCardType);

		Hand hand = new Hand();
		hand.addCard(extraCard);
		hand.addCard(duplicateCard1);
		hand.addCard(duplicateCard2);

		return hand;
	}

	private Hand handWithFiveCardsAndThreeDuplicates() {
		Card extraCard1 = mockCard(CardType.DEFUSE);
		Card extraCard2 = mockCard(CardType.ATTACK);

		CardType duplicateCardType = CardType.FAVOR;
		Card duplicateCard1 = mockCard(duplicateCardType);
		Card duplicateCard2 = mockCard(duplicateCardType);
		Card duplicateCard3 = mockCard(duplicateCardType);

		Hand hand = new Hand();
		hand.addCard(duplicateCard1);
		hand.addCard(duplicateCard2);
		hand.addCard(extraCard1);
		hand.addCard(extraCard2);
		hand.addCard(duplicateCard3);

		return hand;
	}
}