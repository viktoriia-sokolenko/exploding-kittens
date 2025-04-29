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
}
