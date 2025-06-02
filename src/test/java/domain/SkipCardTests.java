package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

public class SkipCardTests {
    private SkipCard skipCard;
    private GameContext mockGameContext;

    @BeforeEach
    void setUp() {
        skipCard = new SkipCard();
        mockGameContext = EasyMock.createMock(GameContext.class);
    }

    @Test
    void constructor_createsCardWithSkipType() {
        assertEquals(CardType.SKIP, skipCard.getCardType());
    }

    @Test
    void createEffect_returnsNonNullEffect() {
        CardEffect effect = skipCard.createEffect();

        assertNotNull(effect, "Effect cannot be null");
    }
}
