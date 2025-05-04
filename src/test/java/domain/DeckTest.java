package domain;

import org.easymock.EasyMock;
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

		Exception exception = assertThrows(NoSuchElementException.class, () -> {
			deck.draw();
		});

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
		assertEquals(0, cardList.size());

	}
}
