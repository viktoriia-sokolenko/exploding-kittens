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
}
