package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.easymock.EasyMock;

public class CardTest {

	private static class TestCard extends Card {
		public TestCard(CardType cardType) {
			super(cardType);
		}

		@Override
		public void play(Player player) {
			java.util.Objects.requireNonNull(player, "Player cannot be null");
		}
	}

	@Test
	public void constructor_WithNullType_ThrowsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new TestCard(null));
	}

	@Test
	public void constructor_WithVa`lidCardType_CreatesCard() {
		CardType type = CardType.EXPLODING_KITTEN;
		Card card = new TestCard(type);
		assertEquals(type, card.getCardType());
	}

	@Test
	public void equals_CompareWithItself_ReturnsTrue() {
		CardType type = CardType.NORMAL;
		Card card = new TestCard(type);
		assertEquals(card, card);
	}

	@Test
	public void equals_CompareWithSameTypeCard_ReturnsTrue() {
		CardType type = CardType.NORMAL;
		Card card1 = new TestCard(type);
		Card card2 = new TestCard(type);
		assertEquals(card1, card2);
	}

	@Test
	public void equals_CompareWithNull_ReturnsFalse() {
		CardType type = CardType.NORMAL;
		Card card = new TestCard(type);
		assertNotEquals(null, card);
	}

	@Test
	public void equals_CompareWithDifferentClass_ReturnsFalse() {
		CardType type = CardType.NORMAL;
		Card card = new TestCard(type);
		Object obj = new Object();
		assertNotEquals(card, obj);
	}

	@Test
	public void hashCode_SameCards_ReturnsSameHashCode() {
		CardType type = CardType.NORMAL;
		Card card1 = new TestCard(type);
		Card card2 = new TestCard(type);
		assertEquals(card1.hashCode(), card2.hashCode());
	}

	@Test
	public void hashCode_DifferentCards_MayReturnDifferentHashCode() {
		Card card1 = new TestCard(CardType.NORMAL);
		Card card2 = new TestCard(CardType.EXPLODING_KITTEN);
		int hashCode1 = card1.hashCode();
		int hashCode2 = card2.hashCode();
		assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void getType_returnsAssignedType() {
		CardType type = CardType.ATTACK;
		Card card = new TestCard(type);
		assertEquals(type, card.getCardType());
	}

	@Test
	public void play_nullPlayer_throwsNullPointerException() {
		Card testCard = new TestCard(CardType.ATTACK);
		assertThrows(NullPointerException.class, () -> testCard.play(null));
	}

	@Test
	public void play_validPlayer_doesNotThrow() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		Card testCard = new TestCard(CardType.ATTACK);
		assertDoesNotThrow(() -> testCard.play(mockPlayer));
	}
}
