package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

	@Test
	public void Constructor_WithNullType_ThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new Card(null));
	}

	@Test
	public void Constructor_WithValidCardType_CreatesCard() {
		CardType type = CardType.EXPLODING_KITTEN;
		Card card = new Card(type);
		assertEquals(type, card.getCardType());
	}

	@Test
	public void Equals_CompareWithItself_ReturnsTrue() {
		CardType type = CardType.NORMAL;
		Card card = new Card(type);
		assertEquals(card, card);
	}

	@Test
	public void Equals_CompareWithSameTypeCard_ReturnsTrue() {
		CardType type = CardType.NORMAL;
		Card card1 = new Card(type);
		Card card2 = new Card(type);
		assertEquals(card1, card2);
	}

	@Test
	public void Equals_CompareWithNull_ReturnsFalse() {
		CardType type = CardType.NORMAL;
		Card card = new Card(type);
		assertNotEquals(null, card);
	}

	@Test
	public void Equals_CompareWithDifferentClass_ReturnsFalse() {
		CardType type = CardType.NORMAL;
		Card card = new Card(type);
		Object obj = new Object();
		assertNotEquals(card, obj);
	}

	@Test
	public void HashCode_SameCards_ReturnsSameHashCode() {
		CardType type = CardType.NORMAL;
		Card card1 = new Card(type);
		Card card2 = new Card(type);
		assertEquals(card1.hashCode(), card2.hashCode());
	}

	@Test
	public void HashCode_DifferentCards_MayReturnDifferentHashCode() {
		Card card1 = new Card(CardType.NORMAL);
		Card card2 = new Card(CardType.EXPLODING_KITTEN);
		int hashCode1 = card1.hashCode();
		int hashCode2 = card2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
	}

}
