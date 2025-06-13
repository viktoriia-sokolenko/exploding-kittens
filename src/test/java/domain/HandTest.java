package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

	@Test
	public void isEmpty_withEmptyHand_returnsTrue() {
		Hand emptyHand = new Hand();
		assertTrue(emptyHand.isEmpty());
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void isEmpty_withOneCardInHand_returnsFalse(CardType testCardType) {
		Hand handWithOneCard = handWithOneCard(testCardType);
		assertFalse(handWithOneCard.isEmpty());
	}

	@Test
	public void isEmpty_withTwoCardsInHand_returnsFalse() {
		Hand hand = handWithTwoCards();
		assertFalse(hand.isEmpty());
	}

	@Test
	public void containsCardType_withNullCardType_throwsNullPointerException() {
		Hand hand = handWithOneCard(CardType.ATTACK);
		assertThrows(NullPointerException.class, () -> hand.containsCardType(null));
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void containsCardType_withEmptyHand_returnsFalse(CardType testCardType) {
		Hand emptyHand = new Hand();
		assertFalse(emptyHand.containsCardType(testCardType));
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void containsCardType_withCardInHand_returnsTrue(CardType testCardType) {
		Hand handWithOneCard = handWithOneCard(testCardType);
		assertTrue(handWithOneCard.containsCardType(testCardType));
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

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void getNumberOfCards_withOneCardInHand_returnsOne(CardType testCardType) {
		Hand handWithOneCard = handWithOneCard(testCardType);
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
		Hand hand = handWithOneCard(CardType.ATTACK);
		assertThrows(NullPointerException.class, () -> hand.addCard(null));
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void addCard_toEmptyHand_insertsCard(CardType testCardType) {
		Card card = mockCard(testCardType);

		Hand hand = new Hand();
		hand.addCard(card);

		int expectedNumberOfCards = 1;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());

		assertTrue(hand.containsCardType(testCardType));
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void addCard_toHandWithOneCard_insertsCard(CardType testCardType) {
		CardType expectedCardType = CardType.SKIP;
		Card card = mockCard(expectedCardType);

		Hand hand = handWithOneCard(testCardType);
		hand.addCard(card);

		int expectedNumberOfCards = 2;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());

		assertTrue(hand.containsCardType(expectedCardType));
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

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void addExplodingKittenCard_throwsIllegalArgumentException(CardType testCardType) {
		CardType expectedCardType = CardType.EXPLODING_KITTEN;
		Card card = mockCard(expectedCardType);

		Hand hand = handWithOneCard(testCardType);
		assertThrows(IllegalArgumentException.class, () -> hand.addCard(card));

	}

	@Test
	public void removeCard_withNullCard_throwsNullPointerException() {
		Hand hand = handWithOneCard(CardType.ATTACK);
		assertThrows(NullPointerException.class, () -> hand.removeCard(null));
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void removeCard_withEmptyHand_throwsIllegalStateException(CardType testCardType) {
		Hand emptyHand = new Hand();
		Card card = mockCard(testCardType);
		assertThrows(IllegalStateException.class, () -> emptyHand.removeCard(card));
	}

	@Test
	public void removeCard_withCardNotInHand_throwsIllegalArgumentException() {
		Hand hand = handWithTwoCards();
		Card card = mockCard(CardType.DEFUSE);
		assertThrows(IllegalArgumentException.class, () -> hand.removeCard(card));
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void removeCard_withOneCardInHand_emptiesHand(CardType testCardType) {
		Card card = mockCard(testCardType);

		Hand hand = handWithOneCard(testCardType);
		hand.removeCard(card);

		assertTrue(hand.isEmpty());
		assertFalse(hand.containsCardType(testCardType));
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
		Hand hand = handWithOneCard(CardType.ATTACK);
		assertThrows(NullPointerException.class, () -> hand.getCountOfCardType(null));
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void getCountOfCardType_withEmptyHand_returnsZero(CardType testCardType) {
		Hand emptyHand = new Hand();

		int expectedCount = 0;
		assertEquals(expectedCount, emptyHand.getCountOfCardType(testCardType));
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void getCountOfCardType_withCardInHand_returnsOne(CardType testCardType) {
		Hand hand = handWithOneCard(testCardType);
		int expectedCount = 1;
		assertEquals(expectedCount, hand.getCountOfCardType(testCardType));
	}

	@Test
	public void getCountOfCardType_withCardNotInHand_returnsZero() {
		Hand hand = handWithTwoCards();
		CardType cardType = CardType.ALTER_THE_FUTURE;
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

	@Test
	public void removeDefuseCard_withEmptyHand_throwsIllegalStateException() {
		Hand emptyHand = new Hand();
		assertThrows(IllegalStateException.class, emptyHand::removeDefuseCard);
	}

	@Test
	public void removeDefuseCard_withCardNotInHand_throwsIllegalArgumentException() {
		Hand hand = handWithTwoCards();
		assertThrows(IllegalArgumentException.class, hand::removeDefuseCard);
	}

	@Test
	public void removeDefuseCard_withCardInHand_removesCard() {
		Hand hand = handWithOneCard(CardType.DEFUSE);
		hand.removeDefuseCard();
		assertTrue(hand.isEmpty());
	}

	@Test
	public void removeDefuseCard_withTwoDefuseCardsInHand_removesOnlyOneCard() {
		Hand hand = handWithTwoDefuseAndOneExtraCards();
		hand.removeDefuseCard();

		assertTrue(hand.containsCardType(CardType.DEFUSE));

		int expectedNumberOfCards = 2;
		assertEquals(expectedNumberOfCards, hand.getNumberOfCards());
	}

	private Card mockCard(CardType type) {
		Card card = EasyMock.createMock(Card.class);
		EasyMock.expect(card.getCardType()).andStubReturn(type);
		EasyMock.replay(card);
		return card;
	}

	private Hand handWithOneCard(CardType cardType) {
		Card mockCard = mockCard(cardType);

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

	private Hand handWithTwoDefuseAndOneExtraCards() {
		Card extraCard = mockCard(CardType.SHUFFLE);
		Card defuseCard1 = mockCard(CardType.DEFUSE);
		Card defuseCard2 = mockCard(CardType.DEFUSE);

		Hand hand = new Hand();
		hand.addCard(defuseCard1);
		hand.addCard(extraCard);
		hand.addCard(defuseCard2);

		return hand;
	}

	@Test
	public void getCardAt_withNegativeIndex_throwsIndexOutOfBoundsException() {
		Hand hand = handWithOneCard(CardType.ATTACK);
		assertThrows(IndexOutOfBoundsException.class, () ->
				hand.getCardAt(-1));
	}

	@Test
	public void getCardAt_withEmptyHand_throwsIndexOutOfBoundsException() {
		Hand emptyHand = new Hand();
		assertThrows(IndexOutOfBoundsException.class, () -> emptyHand.getCardAt(0));
	}
}