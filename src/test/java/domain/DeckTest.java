package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

	static Stream<List<Card>> nonEmptyCardListsWithTwoCards() {
		return Stream.of(
				List.of(new Card(CardType.NORMAL),
						new Card(CardType.ATTACK)),
				List.of(new Card(CardType.DEFUSE),
						new Card(CardType.SKIP)),
				List.of(new Card(CardType.FAVOR),
						new Card(CardType.EXPLODING_KITTEN)),
				List.of(new Card(CardType.SHUFFLE),
						new Card(CardType.ALTER_THE_FUTURE)),
				List.of(new Card(CardType.SEE_THE_FUTURE),
						new Card(CardType.NUKE)));
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
	public void PeakTop_DeckWithTwoCards_ReturnsCardInIndexOne(List<Card> cards) {
		Deck deck = new Deck(cards);

		Card expectedCard = cards.get(1);
		Card actualCard = deck.peekTop();

		assertEquals(expectedCard, actualCard);
	}

	@Test
	public void PeakTop_DeckWithThreeCardsAndDuplicate_ReturnsLastCard() {
		Card card1 = new Card(CardType.SEE_THE_FUTURE);
		Card card2 = new Card(CardType.NORMAL);
		Card card3 = new Card(CardType.NORMAL);
		Deck deck = new Deck(List.of(card1, card2, card3));

		Card actualCard = deck.peekTop();

		assertEquals(card3, actualCard);
	}

	@Test
	public void GetCardAt_LessThanZeroOnEmptyDeck_ThrowsIndexOutOfBoundsException() {
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
	public void GetCardAt_LessThanZeroOnNonEmptyDeck_ThrowsIndexOutOfBoundsException() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.NORMAL);
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
	public void GetCardAt_GreaterThanZeroOnEmptyDeck_ThrowsIndexOutOfBoundsException() {
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
	public void GetCardAt_ZeroOnEmptyDeck_ThrowsIndexOutOfBoundsException() {
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
	public void GetCardAt_ThreeOnDeckWithThreeCards_ThrowsIndexOutOfBoundsException() {
		Card card1 = new Card(CardType.NUKE);
		Card card2 = new Card(CardType.SEE_THE_FUTURE);
		Card card3 = new Card(CardType.DEFUSE);
		int index = 3;

		Deck deck = new Deck(List.of(card1, card2, card3));

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(index);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void GetCardAt_FourDeckWithThreeCards_ThrowsIndexOutOfBoundsException() {
		Card card1 = new Card(CardType.NUKE);
		Card card2 = new Card(CardType.NORMAL);
		Card card3 = new Card(CardType.NORMAL);
		int index = 4;

		Deck deck = new Deck(List.of(card1, card2, card3));

		Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
			Card card = deck.getCardAt(index);
		});

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
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
	public void DrawAndGetDeckSize_DeckWithThreeCardsAndDuplicate() {
		Card card1 = new Card(CardType.SEE_THE_FUTURE);
		Card card2 = new Card(CardType.NORMAL);
		Card card3 = new Card(CardType.NORMAL);

		Deck deck = new Deck(List.of(card1, card2, card3));
		Card actualCard = deck.draw();

		assertEquals(card3, actualCard);
		assertEquals(2, deck.getDeckSize());
	}

	@Test
	public void GetDeckSize_DeckWithTwoCards_ReturnsTwo() {
		Card card1 = new Card(CardType.SEE_THE_FUTURE);
		Card card2 = new Card(CardType.NORMAL);

		Deck deck = new Deck(List.of(card1, card2));

		int expectedSize = 2;
		int actualSize = deck.getDeckSize();

		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void GetDeckSize_DeckWithThreeCardsThatHaveDuplicates_ReturnsThree() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.NORMAL);
		Card card3 = new Card(CardType.NORMAL);

		Deck deck = new Deck(List.of(card1, card2, card3));

		int expectedSize = 3;
		int actualSize = deck.getDeckSize();

		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void InsertCardAt_IndexLessThanZeroOnEmptyDeck_ThrowsIndexOutOfBoundsException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);
		Card card = new Card(CardType.NORMAL);
		int index = -1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class,
				() -> deck.insertCardAt(card, index));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void InsertCardAt_IndexLessThanZeroOnNonEmptyDeck_ThrowsIndexOutOfBoundsException() {
		Card card = new Card(CardType.NORMAL);
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
	public void InsertCardAt_IndexGreaterThanZeroOnEmptyDeck_ThrowsIndexOutOfBoundsException() {
		Card card = new Card(CardType.NORMAL);
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);
		int index = 1;

		Exception exception = assertThrows(IndexOutOfBoundsException.class,
				() -> deck.insertCardAt(card, index));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void InsertCardAt_IndexEqualsThreeOnDeckWithTwo_ThrowsIndexOutOfBoundsException() {
		Card card = new Card(CardType.NORMAL);
		Card card1 = new Card(CardType.EXPLODING_KITTEN);
		Card card2 = new Card(CardType.DEFUSE);
		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));

		Deck deck = new Deck(nonEmptyCardList);
		int index = 3;

		Exception exception = assertThrows(IndexOutOfBoundsException.class,
				() -> deck.insertCardAt(card, index));

		String expectedMessage = "Index out of bounds";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void InsertCardAt_NullCardOnEmptyDeck_ThrowsNullPointerException() {
		List<Card> emptyCardList = new ArrayList<>();

		Deck deck = new Deck(emptyCardList);

		assertThrows(NullPointerException.class, () -> deck.insertCardAt(null, 0));
	}

	@Test
	public void InsertCardAt_NullCardOnNonEmptyDeck_ThrowsNullPointerException() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.FAVOR);

		List<Card> nonEmptyCardList = new ArrayList<>(List.of(card1, card2));
		Deck deck = new Deck(nonEmptyCardList);

		assertThrows(NullPointerException.class, () -> deck.insertCardAt(null, 0));
	}

	@Test
	public void InsertCardAt_IndexEqualsZeroOnEmptyDeck() {
		Card card = new Card(CardType.EXPLODING_KITTEN);
		int index = 0;
		List<Card> cardList = new ArrayList<>();

		Deck deck = new Deck(cardList);
		deck.insertCardAt(card, index);

		assertEquals(1, deck.getDeckSize());
		assertEquals(card, deck.peekTop());
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void InsertCardAtAndGetCardAt_IndexIsZeroOnNonEmptyDeck(List<Card> cards) {
		Card card = new Card(CardType.NORMAL);
		int index = 0;

		Deck deck = new Deck(cards);

		deck.insertCardAt(card, index);
		Card actualCard = deck.getCardAt(index);

		assertEquals(card, actualCard);
		assertEquals(3, deck.getDeckSize());
	}

	@ParameterizedTest
	@MethodSource("nonEmptyCardListsWithTwoCards")
	public void InsertCardAt_IndexIsTwoOnNonEmptyDeckWithTwoElements(List<Card> cards) {
		Card card = new Card(CardType.NORMAL);
		int index = 2;

		Deck deck = new Deck(cards);

		deck.insertCardAt(card, index);

		assertEquals(card, deck.peekTop());
		assertEquals(3, deck.getDeckSize());

	}

	@Test
	public void InsertCardAtAndGetCardAt_IndexIsOneOnNonEmptyDeckWithTwoElements() {
		Card card = new Card(CardType.NORMAL);
		Card card1 = new Card(CardType.EXPLODING_KITTEN);
		Card card2 = new Card(CardType.DEFUSE);
		List<Card> cardsList = new ArrayList<>(List.of(card1, card2));
		int index = 1;

		Deck deck = new Deck(cardsList);
		deck.insertCardAt(card, index);

		assertEquals(card, deck.getCardAt(index));
		assertEquals(3, deck.getDeckSize());
	}

	@Test
	public void ShuffleDeck_EmptyDeck_OrderRemainTheSame() {
		List<Card> cardList = new ArrayList<>();
		Random rand = EasyMock.createMock(Random.class);
		EasyMock.replay(rand);

		Deck deck = new Deck(cardList);

		deck.shuffleDeck(rand);

		assertEquals(0, deck.getDeckSize());
		EasyMock.verify(rand);
	}

	@Test
	public void ShuffleDeck_OneCardinDeck() {
		Card card = new Card(CardType.NORMAL);
		List<Card> cardList = new ArrayList<>(List.of(card));
		Random rand = EasyMock.createMock(Random.class);
		EasyMock.replay(rand);

		Deck deck = new Deck(cardList);
		deck.shuffleDeck(rand);

		assertEquals(1, deck.getDeckSize());
		assertEquals(card, deck.peekTop());
		EasyMock.verify(rand);
	}

	@Test
	public void ShuffleDeck_TwoCardinDeck() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.ATTACK);
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

}
