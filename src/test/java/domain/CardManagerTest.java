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

    @Test
    void playCard_playerDoesNotHaveCard_throwsIllegalArgumentException() {
        EasyMock.expect(skipCard.getCardType()).andReturn(CardType.SKIP);
        EasyMock.expect(player.getCardTypeCount(CardType.SKIP)).andReturn(0);
        EasyMock.replay(skipCard, player);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cardManager.playCard(skipCard, player)
        );
        assertTrue(
                ex.getMessage().contains("Player does not have this card type"),
                "Expected exception message to mention missing card type"
        );

        EasyMock.verify(skipCard, player);
    }

    @Test
    void playCard_playerHasCard_executesCardEffect() {
        EasyMock.expect(skipCard.getCardType()).andReturn(CardType.SKIP);
        EasyMock.expect(player.getCardTypeCount(CardType.SKIP)).andReturn(1);
        player.playCard(EasyMock.anyObject(SkipCard.class));
        EasyMock.expectLastCall().once();
        EasyMock.replay(skipCard);
        EasyMock.replay(player);
        assertDoesNotThrow(() -> cardManager.playCard(skipCard, player));
        EasyMock.verify(skipCard);
        EasyMock.verify(player);
    }

}
