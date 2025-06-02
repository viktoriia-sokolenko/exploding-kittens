package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CardSystemIntegrationTest {

    private CardManager cardManager;
    private Player player;
    private Hand hand;

    @BeforeEach
    void setUp() {
        cardManager = new CardManager();
        hand = new Hand();
        player = new Player(hand);
    }

    @Test
    void playSkipCard_integrationTest_removesCardFromHand() {
        SkipCard skipCard = new SkipCard();
        hand.addCard(skipCard);
        assertEquals(1, player.getCardTypeCount(CardType.SKIP));
        cardManager.playCard(skipCard, player);
        assertEquals(0, player.getCardTypeCount(CardType.SKIP));
    }

    @Test
    void playSkipCard_playerDoesNotHaveCard_throwsException() {
        SkipCard skipCard = new SkipCard();

        assertEquals(0, player.getCardTypeCount(CardType.SKIP));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cardManager.playCard(skipCard, player)
        );

        assertTrue(exception.getMessage().contains("Player does not have this card type"));
    }
}