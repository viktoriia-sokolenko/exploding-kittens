package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeckTest {

	@Test
	public void peekTop_emptyDeck_throwsNoSuchElementException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);

		String expectedMessage = "Deck is empty";

		Exception exception = assertThrows(NoSuchElementException.class, deck::peekTop);

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void peekTop_deckWithOneCard_returnsTheOnlyCard(CardType testCardType) {
		Card expectedCard = mockCard(testCardType);
		List<Card> cardList = new ArrayList<>(List.of(expectedCard));

		Deck deck = new Deck(cardList);
		Card actualCard = deck.peekTop();

		assertEquals(expectedCard, actualCard);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void peekTop_deckWithTwoCards_returnsCardInIndexOne(List<Card> cards) {
		Deck deck = new Deck(cards);

		Card expectedCard = cards.get(1);
		Card actualCard = deck.peekTop();

		assertEquals(expectedCard, actualCard);
	}

	@Test
	public void peekTop_deckWithThreeCardsAndDuplicate_returnsLastCard() {
		Card card1 = mockCard(CardType.SEE_THE_FUTURE);
		Card card2 = mockCard(CardType.NORMAL);
		Card card3 = mockCard(CardType.NORMAL);
		Deck deck = new Deck(List.of(card1, card2, card3));

		Card actualCard = deck.peekTop();

		assertEquals(card3, actualCard);
	}

	@Test
	public void getCardAt_emptyDeckWithIndexNegative_throwsIndexOutOfBoundsException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);
		int index = -1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(index);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getCardAt_nonEmptyDeckWithIndexNegative_throwsIndexOutOfBoundsException() {
		Card card1 = mockCard(CardType.NORMAL);
		Card card2 = mockCard(CardType.NORMAL);
		List<Card> cardList = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(cardList);
		int index = -1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(index);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getCardAt_emptyDeckWithIndexOne_throwsIndexOutOfBoundsException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);
		int index = 1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(index);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getCardAt_emptyDeckWithIndexZero_throwsIndexOutOfBoundsException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);
		int index = 0;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(index);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getCardAt_deckWithThreeCardsWithIndexThree_throwsIndexOutOfBoundsException() {
		Card card1 = mockCard(CardType.NUKE);
		Card card2 = mockCard(CardType.SEE_THE_FUTURE);
		Card card3 = mockCard(CardType.DEFUSE);
		final int INDEX_OUT_OF_BOUNDS = 3;

		Deck deck = new Deck(List.of(card1, card2, card3));

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(INDEX_OUT_OF_BOUNDS);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getCardAt_deckWithTwoCardsWithIndexTwo_throwsIndexOutOfBoundsException() {
		Card card1 = mockCard(CardType.NUKE);
		Card card2 = mockCard(CardType.NORMAL);
		final int INDEX_OUT_OF_BOUNDS = 2;

		Deck deck = new Deck(List.of(card1, card2));

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(INDEX_OUT_OF_BOUNDS);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	public void getCardAt_deckWithThreeCardsWithIndexFour_throwsIndexOutOfBoundsException() {
		Card card1 = mockCard(CardType.NUKE);
		Card card2 = mockCard(CardType.NORMAL);
		Card card3 = mockCard(CardType.NORMAL);
		final int INDEX_OUT_OF_BOUNDS = 4;

		Deck deck = new Deck(List.of(card1, card2, card3));

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(INDEX_OUT_OF_BOUNDS);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void getCardAt_deckWithThreeCardsAndDuplicateWithIndexOne_returnsCardInIndexOne() {
		Card card1 = mockCard(CardType.ALTER_THE_FUTURE);
		Card card2 = mockCard(CardType.SHUFFLE);
		Card card3 = mockCard(CardType.SHUFFLE);

		final int INDEX = 1;
		Deck deck = new Deck(List.of(card1, card2, card3));

		Card actualCard = deck.getCardAt(INDEX);
		assertEquals(card2, actualCard);
	}

	@Test
	public void draw_emptyDeck_throwsNoSuchElementException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);

		String expectedMessage = "Deck is empty";

		Exception exception = assertThrows(NoSuchElementException.class, deck::draw);

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void drawAndGetDeckSize_deckWithOneCard_returnsEmptyDeck(CardType testCardType) {
		Card card = mockCard(testCardType);
		List<Card> cardList = new ArrayList<>(List.of(card));

		Deck deck = new Deck(cardList);

		Card actualCard = deck.draw();

		assertEquals(card, actualCard);
		assertEquals(0, deck.getDeckSize());
	}


	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void drawAndGetDeckSize_deckWithTwoCards_returnsDeckWithOneCard(List<Card> cards) {
		Deck deck = new Deck(cards);

		Card expectedCard = cards.get(1);
		Card actualCard = deck.draw();

		assertEquals(expectedCard, actualCard);
		assertEquals(1, deck.getDeckSize());
	}

	@Test
	public void drawAndGetDeckSize_deckWithThreeCardsAndDuplicate_returnsDeckWithTwoCards() {
		Card card1 = mockCard(CardType.SEE_THE_FUTURE);
		Card card2 = mockCard(CardType.NORMAL);
		Card card3 = mockCard(CardType.NORMAL);

		Deck deck = new Deck(List.of(card1, card2, card3));
		Card actualCard = deck.draw();

		assertEquals(card3, actualCard);
		assertEquals(2, deck.getDeckSize());
	}

	@Test
	public void getDeckSize_deckWithTwoCards_ReturnsTwo() {
		Card card1 = mockCard(CardType.SEE_THE_FUTURE);
		Card card2 = mockCard(CardType.NORMAL);

		Deck deck = new Deck(List.of(card1, card2));

		int expectedSize = 2;
		int actualSize = deck.getDeckSize();

		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void getDeckSize_deckWithThreeCardsAndDuplicate_ReturnsThree() {
		Card card1 = mockCard(CardType.NORMAL);
		Card card2 = mockCard(CardType.NORMAL);
		Card card3 = mockCard(CardType.NORMAL);

		Deck deck = new Deck(List.of(card1, card2, card3));

		final int DECK_SIZE = 3;
		int actualSize = deck.getDeckSize();

		assertEquals(DECK_SIZE, actualSize);
	}

	@Test
	public void insertCardAt_emptyDeckWithIndexNegative_throwsIndexOutOfBoundsException() {
		List<Card> emptyCardList = new ArrayList<>();
		Card card = mockCard(CardType.NORMAL);
		int index = -1;

		Deck deck = new Deck(emptyCardList);

		Exception exception = assertThrows(IndexOutOfBoundsException.class,
				() -> deck.insertCardAt(card, index));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void insertCardAt_nonEmptyDeckWithIndexNegative_throwsIndexOutOfBoundsException() {
		Card card = mockCard(CardType.NORMAL);
		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card));
		int index = -1;

		Deck deck = new Deck(nonEmptyCardList);

		Exception exception = assertThrows(IndexOutOfBoundsException.class,
				() -> deck.insertCardAt(card, index));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void insertCardAt_emptyDeckWithIndexOne_throwsIndexOutOfBoundsException() {
		Card card = mockCard(CardType.NORMAL);
		List<Card> emptyCardList = new ArrayList<>();
		int index = 1;

		Deck deck = new Deck(emptyCardList);

		Exception exception = assertThrows(IndexOutOfBoundsException.class,
				() -> deck.insertCardAt(card, index));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void insertCardAt_deckWithTwoCardsWithIndexThree_throwsIndexOutOfBoundsException() {
		Card card = mockCard(CardType.NORMAL);
		Card card1 = mockCard(CardType.EXPLODING_KITTEN);
		Card card2 = mockCard(CardType.DEFUSE);
		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));

		Deck deck = new Deck(nonEmptyCardList);
		final int INDEX = 3;

		Exception exception = assertThrows(IndexOutOfBoundsException.class,
				() -> deck.insertCardAt(card, INDEX));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void insertCardAt_emptyDeckAndInsertNullCard_throwsNullPointerException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);

		assertThrows(NullPointerException.class, () -> deck.insertCardAt(null, 0));
	}

	@Test
	public void insertCardAt_nonEmptyDeckAndInsertNullCard_throwsNullPointerException() {
		Card card1 = mockCard(CardType.NORMAL);
		Card card2 = mockCard(CardType.FAVOR);

		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(nonEmptyCardList);

		assertThrows(NullPointerException.class, () -> deck.insertCardAt(null, 0));
	}

	@Test
	public void insertCardAt_emptyDeckWithIndexZero() {
		Card card = mockCard(CardType.EXPLODING_KITTEN);
		List<Card> cardList = new ArrayList<>();
		int index = 0;

		Deck deck = new Deck(cardList);
		deck.insertCardAt(card, index);

		assertEquals(1, deck.getDeckSize());
		assertEquals(card, deck.peekTop());
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void insertCardAtAndGetCardAt_nonEmptyDeckWithIndexZero(List<Card> cards) {
		Card card = mockCard(CardType.NORMAL);
		int index = 0;
		final int FINAL_SIZE = 3;

		Deck deck = new Deck(cards);

		deck.insertCardAt(card, index);
		Card actualCard = deck.getCardAt(index);

		assertEquals(card, actualCard);
		assertEquals(FINAL_SIZE, deck.getDeckSize());
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void insertCardAt_deckWithTwoCardsWithIndexTwo(List<Card> cards) {
		Card card = mockCard(CardType.NORMAL);
		int index = 2;
		final int FINAL_SIZE = 3;

		Deck deck = new Deck(cards);

		deck.insertCardAt(card, index);

		assertEquals(card, deck.peekTop());
		assertEquals(FINAL_SIZE, deck.getDeckSize());

	}

	@Test
	public void insertCardAtAndGetCardAt_deckWithTwoCardsWithIndexOne() {
		Card card = mockCard(CardType.SKIP);
		Card card1 = mockCard(CardType.SHUFFLE);
		Card card2 = mockCard(CardType.NUKE);
		List<Card> cardsList = new ArrayList<>(List.of(card1, card2));
		int index = 1;
		final int FINAL_SIZE = 3;

		Deck deck = new Deck(cardsList);
		deck.insertCardAt(card, index);

		assertEquals(card, deck.getCardAt(index));
		assertEquals(FINAL_SIZE, deck.getDeckSize());
	}

	@Test
	public void shuffleDeck_emptyDeckAndNullRandom_throwsNullPointerException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);

		assertThrows(NullPointerException.class, () -> deck.shuffleDeck(null));
	}

	@Test
	public void shuffleDeck_emptyDeck_orderRemainTheSame() {
		List<Card> cardList = new ArrayList<>();
		Random rand = EasyMock.createMock(Random.class);
		EasyMock.replay(rand);

		Deck deck = new Deck(cardList);

		deck.shuffleDeck(rand);

		assertEquals(0, deck.getDeckSize());
		EasyMock.verify(rand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void shuffleDeck_deckWithOneCard_orderRemainTheSame(CardType testCardType) {
		Card card = mockCard(testCardType);
		List<Card> cardList = new ArrayList<>(List.of(card));
		Random rand = EasyMock.createMock(Random.class);
		EasyMock.replay(rand);

		Deck deck = new Deck(cardList);
		deck.shuffleDeck(rand);

		assertEquals(1, deck.getDeckSize());
		assertEquals(card, deck.peekTop());
		EasyMock.verify(rand);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void shuffleDeck_deckWithTwoCards_orderFlipsPositions(List<Card> cards) {
		Card card1 = cards.get(0);
		Card card2 = cards.get(1);
		List<Card> cardsList = new ArrayList<>(List.of(card1, card2));
		Random rand = EasyMock.createMock(Random.class);
		Deck deck = new Deck(cardsList);

		EasyMock.expect(rand.nextInt(2)).andReturn(0);
		EasyMock.replay(rand);

		deck.shuffleDeck(rand);

		Card actualCard1 = deck.getCardAt(0);
		Card actualCard2 = deck.getCardAt(1);

		assertEquals(card2, actualCard1);
		assertEquals(card1, actualCard2);
		EasyMock.verify(rand);
	}

	@Test
	public void shuffleDeck_deckWithThreeCards_orderChanges() {
		Card card1 = mockCard(CardType.DEFUSE);
		Card card2 = mockCard(CardType.FAVOR);
		Card card3 = mockCard(CardType.EXPLODING_KITTEN);
		final int SHUFFLE_ROUND1 = 3;
		final int SHUFFLE_ROUND2 = 2;

		List<Card> cardsList = new ArrayList<>(List.of(card1, card2, card3));
		Random rand = EasyMock.createMock(Random.class);
		Deck deck = new Deck(cardsList);

		EasyMock.expect(rand.nextInt(SHUFFLE_ROUND1)).andReturn(1);
		EasyMock.expect(rand.nextInt(SHUFFLE_ROUND2)).andReturn(0);
		EasyMock.replay(rand);

		deck.shuffleDeck(rand);

		Card actualCard1 = deck.getCardAt(0);
		Card actualCard2 = deck.getCardAt(1);
		Card actualCard3 = deck.getCardAt(2);

		assertEquals(card3, actualCard1);
		assertEquals(card1, actualCard2);
		assertEquals(card2, actualCard3);
		EasyMock.verify(rand);
	}

	@Test
	public void shuffleDeck_deckWithThreeCardsAndDuplicate_orderChanges() {
		Card card1 = mockCard(CardType.SHUFFLE);
		Card card2 = mockCard(CardType.ALTER_THE_FUTURE);
		Card card3 = mockCard(CardType.ALTER_THE_FUTURE);
		final int SHUFFLE_ROUND1 = 3;
		final int SHUFFLE_ROUND2 = 2;

		List<Card> cardsList = new ArrayList<>(List.of(card1, card2, card3));
		Random rand = EasyMock.createMock(Random.class);
		Deck deck = new Deck(cardsList);

		EasyMock.expect(rand.nextInt(SHUFFLE_ROUND1)).andReturn(0);
		EasyMock.expect(rand.nextInt(SHUFFLE_ROUND2)).andReturn(0);
		EasyMock.replay(rand);

		deck.shuffleDeck(rand);

		Card actualCard1 = deck.getCardAt(0);
		Card actualCard2 = deck.getCardAt(1);
		Card actualCard3 = deck.getCardAt(2);

		assertEquals(card2, actualCard1);
		assertEquals(card3, actualCard2);
		assertEquals(card1, actualCard3);
		EasyMock.verify(rand);
	}

	@Test
	public void peekTopTwoCards_emptyDeck_throwsNoSuchElementException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);

		String expectedMessage = "Deck is empty";

		Exception exception = assertThrows(NoSuchElementException.class,
				deck::peekTopTwoCards);

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void peekTopTwoCards_deckWithOneCard_returnsTheOnlyCard(CardType testCardType) {
		Card expectedCard = mockCard(testCardType);
		List<Card> expectedCardList = new ArrayList<>(List.of(expectedCard));

		Deck deck = new Deck(expectedCardList);
		List <Card> actualCardList = deck.peekTopTwoCards();

		assertEquals(expectedCardList, actualCardList);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void peekTopTwoCards_deckWithTwoCards_returnsTwoLastCards(List<Card> cards) {
		Deck deck = new Deck(cards);

		Card expectedCard1 = cards.get(1);
		Card expectedCard2 = cards.get(0);
		List <Card> expectedCardList = new ArrayList<>(
				List.of(expectedCard1, expectedCard2));
		List <Card> actualCardList = deck.peekTopTwoCards();

		assertEquals(expectedCardList, actualCardList);
	}

	@Test
	public void peekTopTwoCards_deckWithThreeCardsAndDuplicate_returnsLastDuplicateCards() {
		Deck deck = deckWithThreeCardsAndDuplicate();
		Card duplicateCard1 = deck.getCardAt(2);
		Card duplicateCard2 = deck.getCardAt(1);
		List <Card> expectedCardList = new ArrayList<>(
				List.of(duplicateCard1, duplicateCard2));

		List <Card> actualCardList = deck.peekTopTwoCards();
		assertEquals(expectedCardList, actualCardList);
	}

	@Test
	public void peekTopThreeCards_emptyDeck_throwsNoSuchElementException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);

		String expectedMessage = "Deck is empty";

		Exception exception = assertThrows(NoSuchElementException.class,
				deck::peekTopThreeCards);

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void peekTopThreeCards_deckWithOneCard_returnsTheOnlyCard(CardType testCardType) {
		Card expectedCard = mockCard(testCardType);
		List<Card> expectedCardList = new ArrayList<>(List.of(expectedCard));

		Deck deck = new Deck(expectedCardList);
		List <Card> actualCardList = deck.peekTopThreeCards();

		assertEquals(expectedCardList, actualCardList);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void peekTopThreeCards_deckWithTwoCards_returnsTwoLastCards(List<Card> cards) {
		Deck deck = new Deck(cards);

		Card expectedCard1 = cards.get(1);
		Card expectedCard2 = cards.get(0);
		List <Card> expectedCardList = new ArrayList<>(
				List.of(expectedCard1, expectedCard2));
		List <Card> actualCardList = deck.peekTopThreeCards();

		assertEquals(expectedCardList, actualCardList);
	}

	@Test
	public void peekTopThreeCards_deckWithThreeCards_returnsThreeLastCards() {
		Deck deck = deckWithThreeCards();
		Card expectedCard1 = deck.getCardAt(2);
		Card expectedCard2 = deck.getCardAt(1);
		Card expectedCard3 = deck.getCardAt(0);
		List <Card> expectedCardList = new ArrayList<>(
				List.of(expectedCard1, expectedCard2, expectedCard3));

		List <Card> actualCardList = deck.peekTopThreeCards();
		assertEquals(expectedCardList, actualCardList);
	}

	@Test
	public void peekTopThreeCards_deckWithFourCardsAndDuplicate_returnsLastDuplicateCards() {
		Deck deck = deckWithFourCardsAndDuplicate();
		int indexSizeMinusOne = deck.getDeckSize() - 1;
		Card expectedCard1 = deck.getCardAt(indexSizeMinusOne);
		Card expectedCard2 = deck.getCardAt(2);
		Card expectedCard3 = deck.getCardAt(1);
		List <Card> expectedCardList = new ArrayList<>(
				List.of(expectedCard1, expectedCard2, expectedCard3));

		List <Card> actualCardList = deck.peekTopThreeCards();
		assertEquals(expectedCardList, actualCardList);
	}

	@Test
	public void rearrangeTopThreeCards_emptyDeck_throwsNoSuchElementException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);
		String expectedMessage = "Deck is empty";

		List<Integer> listOfIndices = new ArrayList<>(List.of(0, 1, 2));

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void rearrangeTopThreeCards_oneCardDeckWithTwoIndices_throwsIllegalArgumentException(
			CardType testCardType) {
		Card testCard = mockCard(testCardType);
		Deck deck = new Deck(List.of(testCard));

		String expectedMessage = "Number of indices is larger than the deck size";

		List<Integer> listOfIndices = new ArrayList<>(List.of(0, 1));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void rearrangeTopThreeCards_TwoCardsThreeIndices_throwsIllegalArgumentException(
			List<Card> cards) {
		Deck deck = new Deck(cards);

		String expectedMessage = "Number of indices is larger than the deck size";

		List<Integer> listOfIndices = new ArrayList<>(List.of(0, 1, 2));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void rearrangeTopThreeCards_ThreeCardsFourIndices_throwsIllegalArgumentException() {
		Deck deck = deckWithThreeCards();

		String expectedMessage = "Number of indices is larger than the deck size";

		int extraIndex = deck.getDeckSize();
		List<Integer> listOfIndices = new ArrayList<>(List.of(0, 1, 2, extraIndex));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);

	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void rearrangeTopThreeCards_withNegativeFirstIndex_throwsIllegalArgumentException(
			List<Card> cards) {
		Deck deck = new Deck(cards);

		String expectedMessage = "Negative indices are not allowed";

		List<Integer> listOfIndices = new ArrayList<>(List.of(-1, 1));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void rearrangeTopThreeCards_withNegativeSecondIndex_throwsIllegalArgumentException(
			List<Card> cards) {
		Deck deck = new Deck(cards);

		String expectedMessage = "Negative indices are not allowed";

		List<Integer> listOfIndices = new ArrayList<>(List.of(0, -1));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void rearrangeTopThreeCards_ThreeCardsIndexThree_throwsIllegalArgumentException() {
		Deck deck = deckWithThreeCards();

		String expectedMessage =
				"With deck size s, indices must be [s - 1, s - 3]";

		int wrongIndex = deck.getDeckSize();
		List<Integer> listOfIndices = new ArrayList<>(List.of(wrongIndex, 1, 0));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void rearrangeTopThreeCards_FourCardsIndexZero_throwsIllegalArgumentException() {
		Deck deck = deckWithFourCardsAndDuplicate();

		String expectedMessage =
				"With deck size s, indices must be [s - 1, s - 3]";

		int indexForTopCard = deck.getDeckSize() - 1;
		List<Integer> listOfIndices = new ArrayList<>(List.of(indexForTopCard, 0, 2));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void rearrangeTopThreeCards_withSameFirstThirdIndex_throwsIllegalArgumentException()
	{
		Deck deck = deckWithFourCardsAndDuplicate();

		String expectedMessage =
				"Duplicate indices are not allowed";

		List<Integer> listOfIndices = new ArrayList<>(List.of(1, 2, 1));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void rearrangeTopThreeCards_withSameFirstSecondIndex_throwsIllegalArgumentException(
			List<Card> cards
	)
	{
		Deck deck = new Deck(cards);

		String expectedMessage =
				"Duplicate indices are not allowed";

		List<Integer> listOfIndices = new ArrayList<>(List.of(0, 0));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void rearrangeTopThreeCards_withSameSecondThirdIndex_throwsIllegalArgumentException()
	{
		Deck deck = deckWithThreeCards();

		String expectedMessage =
				"Duplicate indices are not allowed";

		List<Integer> listOfIndices = new ArrayList<>(List.of(0, 2, 2));

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> deck.rearrangeTopThreeCards(listOfIndices));

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void rearrangeTopThreeCards_deckWithOneCard_orderRemainsTheSame(
			CardType testCardType) {
		Card testCard = mockCard(testCardType);
		Deck deck = new Deck(List.of(testCard));

		List<Integer> listOfIndices = new ArrayList<>(List.of(0));
		List<Card> previousTopCards = deck.peekTopThreeCards();
		deck.rearrangeTopThreeCards(listOfIndices);

		List<Card> nextTopCards = deck.peekTopThreeCards();
		assertEquals(previousTopCards, nextTopCards);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void rearrangeTopThreeCards_deckWithTwoCardsAndSameIndices_orderRemainsTheSame(
			List<Card> cards) {
		Deck deck = new Deck(cards);

		List<Integer> listOfIndices = new ArrayList<>(List.of(1, 0));
		List<Card> previousTopCards = deck.peekTopThreeCards();
		deck.rearrangeTopThreeCards(listOfIndices);

		List<Card> nextTopCards = deck.peekTopThreeCards();
		assertEquals(previousTopCards, nextTopCards);
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void rearrangeTopThreeCards_deckWithTwoCardsAndReversedIndices_reversesCards(
			List<Card> cards) {
		Deck deck = new Deck(cards);

		List<Integer> listOfIndices = new ArrayList<>(List.of(0, 1));
		List<Card> previousTopCards = deck.peekTopThreeCards();
		deck.rearrangeTopThreeCards(listOfIndices);
		List<Card> nextTopCards = deck.peekTopThreeCards();

		assertEquals(previousTopCards.get(0), nextTopCards.get(1));
		assertEquals(previousTopCards.get(1), nextTopCards.get(0));
	}

	@Test
	public void rearrangeTopThreeCards_deckWithThreeCardsAndSameIndices_orderRemainsTheSame()
	{
		Deck deck = deckWithThreeCards();

		List<Integer> listOfIndices = new ArrayList<>(List.of(2, 1, 0));
		List<Card> previousTopCards = deck.peekTopThreeCards();
		deck.rearrangeTopThreeCards(listOfIndices);

		List<Card> nextTopCards = deck.peekTopThreeCards();
		assertEquals(previousTopCards, nextTopCards);
	}

	@Test
	public void rearrangeTopThreeCards_deckWithThreeCardsAndDifferentIndices_changesOrder()
	{
		Deck deck = deckWithThreeCards();

		List<Integer> listOfIndices = new ArrayList<>(List.of(1, 0, 2));
		List<Card> previousTopCards = deck.peekTopThreeCards();
		deck.rearrangeTopThreeCards(listOfIndices);
		List<Card> nextTopCards = deck.peekTopThreeCards();

		assertEquals(previousTopCards.get(0), nextTopCards.get(2));
		assertEquals(previousTopCards.get(1), nextTopCards.get(0));
		assertEquals(previousTopCards.get(2), nextTopCards.get(1));
	}

	Stream<List<Card>> nonEmptyCardListsWithTwoCards() {
		return Stream.of(
				List.of(mockCard(CardType.NORMAL),
						mockCard(CardType.ATTACK)),
				List.of(mockCard(CardType.DEFUSE),
						mockCard(CardType.SKIP)),
				List.of(mockCard(CardType.FAVOR),
						mockCard(CardType.EXPLODING_KITTEN)),
				List.of(mockCard(CardType.SHUFFLE),
						mockCard(CardType.ALTER_THE_FUTURE)),
				List.of(mockCard(CardType.SEE_THE_FUTURE),
						mockCard(CardType.NUKE)));
	}

	private Deck deckWithThreeCardsAndDuplicate() {
		Card card1 = mockCard(CardType.SEE_THE_FUTURE);
		Card card2 = mockCard(CardType.NORMAL);
		Card card3 = mockCard(CardType.NORMAL);
		return new Deck(List.of(card1, card2, card3));
	}

	private Deck deckWithThreeCards() {
		Card card1 = mockCard(CardType.ALTER_THE_FUTURE);
		Card card2 = mockCard(CardType.NUKE);
		Card card3 = mockCard(CardType.FAVOR);
		return new Deck(List.of(card1, card2, card3));
	}

	private Deck deckWithFourCardsAndDuplicate() {
		Card card1 = mockCard(CardType.SEE_THE_FUTURE);
		Card card2 = mockCard(CardType.SKIP);
		Card card3 = mockCard(CardType.NORMAL);
		Card card4 = mockCard(CardType.NORMAL);
		return new Deck(List.of(card1, card2, card3, card4));
	}

	private Card mockCard(CardType type) {
		Card card = EasyMock.createMock(Card.class);
		EasyMock.expect(card.getCardType()).andStubReturn(type);
		EasyMock.replay(card);
		return card;
	}
}
