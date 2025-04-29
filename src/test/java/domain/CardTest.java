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
        assertTrue(card.equals(card));
    }

    @Test
    public void Equals_CompareWithSameTypeCard_ReturnsTrue() {
        CardType type = CardType.NORMAL;
        Card card1 = new Card(type);
        Card card2 = new Card(type);
        assertTrue(card1.equals(card2));
    }

    @Test
    public void Equals_CompareWithNull_ReturnsFalse() {
        CardType type = CardType.NORMAL;
        Card card = new Card(type);
        assertFalse(card.equals(null));
    }

    @Test
    public void Equals_CompareWithDifferentClass_ReturnsFalse() {
        CardType type = CardType.NORMAL;
        Card card = new Card(type);
        Object obj = new Object();
        assertFalse(card.equals(obj));
    }
}
