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
    private SkipCard skipCard;

    @BeforeEach
    void setUp() {
        cardManager = new CardManager();
        player  = EasyMock.createMock(Player.class);
        hand = EasyMock.createMock(Hand.class);
        skipCard = EasyMock.createMock(SkipCard.class);
    }

    @Test
    void playCard_withNullCard_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            cardManager.playCard(null, player);
        });
    }

    @Test
    void playCard_withNullPlayer_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            cardManager.playCard(skipCard, null);
        });
    }
}
