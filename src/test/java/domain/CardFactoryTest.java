package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardFactoryTest {
	@Test
	public void createCard_WithNullType_ThrowsNullPointerException() {
		CardFactory factory = new CardFactory();
		assertThrows(NullPointerException.class, () -> factory.createCard(null));
	}
}