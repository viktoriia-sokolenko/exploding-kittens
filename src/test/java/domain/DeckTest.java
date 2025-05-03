package domain;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTest {
	@Test
	public void Draw_WithEmptyDeck_ThrowsNoSuchElementException() {
		Deck deck = new Deck();

		String expectedMessage = "Deck is empty";

		Exception exception = assertThrows(NoSuchElementException.class, () -> {
			deck.draw();
		});

		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}
}
