package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTest {
	@Test
	public void getCardTypeCount_withNullCardType_throwsNullPointerException() {
		Player player = new Player();
		assertThrows(NullPointerException.class, () -> player.getCardTypeCount(null));
	}
}
