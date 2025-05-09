package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HandTest {
	@Test
	public void isEmpty_onEmptyHand_returnsTrue() {
		Hand hand = new Hand();
		assertTrue(hand.isEmpty());
	}
}