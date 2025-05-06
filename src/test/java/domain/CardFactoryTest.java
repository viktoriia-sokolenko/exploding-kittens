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
        assertInstanceOf(ExpoldingKittenCard.class, expoldingKittenCard);
    }

    @Test
    public void CreateCard_WithDefuseCardType_CreatesCard() {
        CardFactory factory = new CardFactory();
        Card defuseCard = factory.createCard(CardType.DEFUSE);
        assertInstanceOf(DefuseCard.class, defuseCard);
    }

    @Test public void CreateCard_WithUnknownCardType_ThrowsIllegalArgumentException() {
        CardFactory factory = new CardFactory();
        assertThrows(IllegalArgumentException.class, () -> factory.createCard(null));

    }
}