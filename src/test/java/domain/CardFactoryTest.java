package domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CardFactoryTest {

    @Test
    public void CreateCard_WithNullType_ThrowsNullPointerException() {
        CardFactory factory = new CardFactory();
        assertThrows(NullPointerException.class, () -> factory.createCard(null));
    }

    @Test
    public void CreateCard_WithExplodingKittenCardType_CreatesCard() {
        CardFactory factory = new CardFactory();
        Card expoldingKittenCard = factory.createCard(CardType.EXPLODING_KITTEN);
        assertTrue(expoldingKittenCard instanceof ExpoldingKittenCard);
    }
}
