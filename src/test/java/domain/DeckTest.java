package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTest {

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

	@Test
	public void DrawAndGetDeckSize_WithTwoCards() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.SEE_THE_FUTURE);
		List<Card> cardList = new ArrayList<>(List.of(card1, card2));

		Deck deck = new Deck(cardList);

		Card actualCard = deck.draw();
		assertEquals(card2, actualCard);

		assertEquals(1, deck.getDeckSize());

	}

	@Test
	public void InsertAt_IndexLessThanZeroOnEmptyList_ThrowsIndexOutOfBoundsException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);
		Card card = new Card(CardType.NORMAL);
		int index = -1;

		Exception exception = assertThrows(
				IndexOutOfBoundsException.class,
				() -> deck.insertAt(index, card)
		);

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

		Exception exception = assertThrows(
				IndexOutOfBoundsException.class, () -> deck.insertAt(index, card)
		);

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

		Exception exception = assertThrows(
				IndexOutOfBoundsException.class,
				() -> deck.insertAt(index, card)
		);

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

		Exception exception = assertThrows(
				IndexOutOfBoundsException.class,
				() -> deck.insertAt(index, card)
		);

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);

	}

	@Test
	public void InsertAt_NullCardOnEmptyList_ThrowsNullPointerException() {
		List<Card> emptyCardList = new ArrayList<>();
		Deck deck = new Deck(emptyCardList);

		assertThrows(NullPointerException.class,
				() -> deck.insertAt(0, null)
		);

	}

	@Test
	public void InsertAt_NullCardOnNonEmptyList_ThrowsNullPointerException() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.FAVOR);

		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(nonEmptyCardList);

		assertThrows(NullPointerException.class,
				() -> deck.insertAt(0, null)
		);
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
