package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTest {
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
	public void Draw_WithOneCard_ReturnsCardWithSizeNowZero() {
		CardType cardType = CardType.NORMAL;
		Card card = new Card(cardType);
		List<Card> cardList = new ArrayList<>(List.of(card));

		Deck deck = new Deck(cardList);

		Card actualCard = deck.draw();

		assertEquals(card, actualCard);
		assertEquals(0, deck.getDeckSize());

	}

	@Test
	public void Draw_WithTwoCards_ReturnsCardWithSizeOne() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.NORMAL);
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
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.NORMAL);

		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(nonEmptyCardList);
		int index = 3;

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> deck.insertAt(index, card));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);

	}


}
