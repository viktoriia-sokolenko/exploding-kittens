package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

public class GameContextTest {
    private GameContext gameContext;
    private Player mockCurrentPlayer;

    @BeforeEach
    public void setUp() {
        mockCurrentPlayer = EasyMock.createMock(Player.class);
        gameContext = new GameContext(mockCurrentPlayer);
    }

    @Test
    void constructor_withValidPlayer_createsGameContext() {
        assertNotNull(gameContext);
    }

    @Test
    void constructor_withNullPlayer_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new GameContext(null);
        });
    }

    @Test
    void getCurrentPlayer_returnsCurrentPlayer() {
        // Act
        Player result = gameContext.getCurrentPlayer();

        // Assert
        assertEquals(mockCurrentPlayer, result);
    }

    @Test
    void playCardFromCurrentPlayerHand_callsPlayerPlayCard() {
        Card testCard = new SkipCard();
        mockCurrentPlayer.playCard(testCard);
        expectLastCall().once();
        replay(mockCurrentPlayer);

        gameContext.playCardFromCurrentPlayerHand(testCard);

        verify(mockCurrentPlayer);
    }
}
