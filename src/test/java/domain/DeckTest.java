package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

	static Stream<List<Card>> nonEmptyCardListsWithTwoCards() {
		return Stream.of(List.of(new Card(CardType.NORMAL), new Card(CardType.ATTACK)), List.of(new Card(CardType.DEFUSE), new Card(CardType.SKIP)), List.of(new Card(CardType.FAVOR), new Card(CardType.EXPLODING_KITTEN)), List.of(new Card(CardType.SHUFFLE), new Card(CardType.ALTER_THE_FUTURE)), List.of(new Card(CardType.SEE_THE_FUTURE), new Card(CardType.NUKE)));
	}

	@Test
	public void PeekTop_EmptyDeck_ThrowsIllegalOperationException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);

		String expectedMessage = "Deck is empty";

		Exception exception = assertThrows(NoSuchElementException.class, deck::peekTop);

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	public void PeekTop_DeckWithOneCard_ReturnsTheOnlyCard() {
		CardType cardType = CardType.NORMAL;
		Card expectedCard = new Card(cardType);
		List<Card> cardList = new ArrayList<>(List.of(expectedCard));

		Deck deck = new Deck(cardList);
		Card actualCard = deck.peekTop();

		assertEquals(expectedCard, actualCard);

	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	void PeakTop_DeckWithTwoCards_ReturnsCardInIndexOne(List<Card> cards) {
		Deck deck = new Deck(cards);

		Card expectedCard = cards.get(1);
		Card actualCard = deck.peekTop();

		assertEquals(expectedCard, actualCard);
	}

	@Test
	void PeakTop_DeckWithThreeCardsAndDuplicate_ReturnsLastCard() {
		Card card1 = new Card(CardType.SEE_THE_FUTURE);
		Card card2 = new Card(CardType.NORMAL);
		Card card3 = new Card(CardType.NORMAL);
		Deck deck = new Deck(List.of(card1, card2, card3));

		Card actualCard = deck.peekTop();

		assertEquals(card3, actualCard);
	}

	@Test
	public void Draw_WithEmptyDeck_ThrowsNoSuchElementException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);

		String expectedMessage = "Deck is empty";

		Exception exception = assertThrows(NoSuchElementException.class, deck::draw);

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void DrawAndGetDeckSize_WithOneCard() {
		CardType cardType = CardType.NORMAL;
		Card card = new Card(cardType);
		List<Card> cardList = new ArrayList<>(List.of(card));

		Deck deck = new Deck(cardList);

		Card actualCard = deck.draw();

		assertEquals(card, actualCard);
		assertEquals(0, deck.getDeckSize());

	}


	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void DrawAndGetDeckSize_WithTwoCards(List<Card> cards) {
		Deck deck = new Deck(cards);

		Card expectedCard = cards.get(1);
		Card actualCard = deck.draw();

		assertEquals(expectedCard, actualCard);
		assertEquals(1, deck.getDeckSize());

	}

	@Test
	public void InsertAt_IndexLessThanZeroOnEmptyList_ThrowsIndexOutOfBoundsException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);
		Card card = new Card(CardType.NORMAL);
		int index = -1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(index, card));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	public void InsertAt_IndexLessThanZeroOnNonEmptyList_ThrowsIndexOutOfBoundsException() {
		Card card = new Card(CardType.NORMAL);
		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card));
		Deck deck = new Deck(nonEmptyCardList);
		int index = -1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(index, card));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	public void InsertAt_IndexGreaterThanZeroOnEmptyList_ThrowsIndexOutOfBoundsException() {
		Card card = new Card(CardType.NORMAL);
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);
		int index = 1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(index, card));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	public void InsertAt_IndexGreaterThanZeroOnNonEmptyList_ThrowsIndexOutOfBoundsException() {
		Card card = new Card(CardType.NORMAL);
		Card card1 = new Card(CardType.EXPLODING_KITTEN);
		Card card2 = new Card(CardType.DEFUSE);

		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(nonEmptyCardList);
		int index = 3;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(index, card));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	public void InsertAt_NullCardOnEmptyList_ThrowsNullPointerException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);

		assertThrows(NullPointerException.class, () -> deck.insertAt(0, null));

	}

	@Test
	public void InsertAt_NullCardOnNonEmptyList_ThrowsNullPointerException() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.FAVOR);

		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(nonEmptyCardList);

		assertThrows(NullPointerException.class, () -> deck.insertAt(0, null));
	}

//	@Test
//	public void InsertAt_IndexEqualsZeroOnEmptyList_CardInsertsAtTop() {
//		Card card = new Card(CardType.ATTACK);
//		int index = 0;
//		List<Card> cardList = new ArrayList<>();
//
//		Deck deck = new Deck(cardList);
//		deck.insertAt(index, card);
//
//		assertEquals(1, deck.getDeckSize());
//	}
}
