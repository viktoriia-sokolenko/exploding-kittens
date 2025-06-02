package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardManagerTest {
    private CardManager cardManager;
    private Player player;
    private Hand hand;
    private Card card;

    @BeforeEach
    void setUp() {
        cardManager = new CardManager();
        mockPlayer = EasyMock.createMock(Player.class);
        mockHand = EasyMock.createMock(Hand.class);
        skipCard = new SkipCard();
    }

    @Test
    void playCard_withNullCard_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            cardManager.playCard(null, player);
        });
    }
}
