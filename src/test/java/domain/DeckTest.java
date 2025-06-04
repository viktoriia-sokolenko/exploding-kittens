package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
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

	@Test
	public void peekTop_deckWithOneCard_returnsTheOnlyCard() {
		Card expectedCard = mockCard(CardType.NORMAL);
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
		List<Card> emptyCardList = new ArrayList<>(List.of(card1, card2));
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

	@Test
	public void drawAndGetDeckSize_deckWithOneCard_returnsEmptyDeck() {
		Card card = mockCard(CardType.NORMAL);
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

	@Test
	public void shuffleDeck_deckWithOneCard_orderRemainTheSame() {
		Card card = mockCard(CardType.SKIP);
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

	private Card mockCard(CardType type) {
		Card card = EasyMock.createMock(Card.class);
		EasyMock.expect(card.getCardType()).andStubReturn(type);
		EasyMock.replay(card);
		return card;
	}
}
