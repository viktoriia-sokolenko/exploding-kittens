package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.easymock.EasyMock;

public class CardTest {

	@Test
	public void constructor_withNullType_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> new TestCard(null));
	}

	@Test
	public void constructor_withValidCardType_createsCard() {
		CardType type = CardType.EXPLODING_KITTEN;
		Card card = new TestCard(type);
		assertEquals(type, card.getCardType());
	}

	@Test
	public void equals_compareWithItself_returnsTrue() {
		CardType type = CardType.NORMAL;
		Card card = new TestCard(type);
		assertEquals(card, card);
	}

	@Test
	public void equals_compareWithSameTypeCard_returnsTrue() {
		CardType type = CardType.NORMAL;
		Card card1 = new TestCard(type);
		Card card2 = new TestCard(type);
		assertEquals(card1, card2);
	}

	@Test
	public void equals_compareWithNull_returnsFalse() {
		CardType type = CardType.NORMAL;
		Card card = new TestCard(type);
		assertNotEquals(null, card);
	}

	@Test
	public void equals_compareWithDifferentClass_returnsFalse() {
		CardType type = CardType.NORMAL;
		Card card = new TestCard(type);
		Object obj = new Object();
		assertNotEquals(card, obj);
	}

	@Test
	public void hashCode_sameCards_returnsSameHashCode() {
		CardType type = CardType.NORMAL;
		Card card1 = new TestCard(type);
		Card card2 = new TestCard(type);
		assertEquals(card1.hashCode(), card2.hashCode());
	}

	@Test
	public void hashCode_differentCards_mayReturnDifferentHashCode() {
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
	public void equals_compareWithDifferentCardSubclass_returnsFalse() {
		CardType type = CardType.NORMAL;
		Card card1 = new TestCard(type);
		Card card2 = new AnotherTestCard(type);
		assertNotEquals(card1, card2);
	}

	@Test
	public void equals_compareWithDifferentCardType_returnsFalse() {
		Card card1 = new TestCard(CardType.NORMAL);
		Card card2 = new TestCard(CardType.ATTACK);
		assertNotEquals(card1, card2);
	}

	@Test
	public void equals_nullCheckComesBefore_CheckIfClassesNotEqual() {
		Card card = new TestCard(CardType.NORMAL);

		assertFalse(card.equals(null));
		boolean result = card.equals(null);
		assertFalse(result);
	}

	@Test
	public void equals_checkIfTwoCardsAreEqual_returnsTrue() {
		Card card1 = new TestCard(CardType.NORMAL);
		Card card2 = new TestCard(CardType.NORMAL);

		assertTrue(card1.equals(card2));
		assertTrue(card2.equals(card1));
	}

	private static class TestCard extends Card {
		public TestCard(CardType cardType) {
			super(cardType);
		}

		@Override
		public CardEffect createEffect() {
			return new CardEffect() {
				@Override
				public void execute(GameContext context) {
				}
			};
		}
	}

	private static class AnotherTestCard extends Card {
		public AnotherTestCard(CardType cardType) {
			super(cardType);
		}

		@Override
		public CardEffect createEffect() {
			return new CardEffect() {
				@Override
				public void execute(GameContext context) {
				}
			};
		}
	}
}
