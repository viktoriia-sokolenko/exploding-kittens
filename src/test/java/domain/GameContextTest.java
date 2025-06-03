package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.UserInterface;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

public class GameContextTest {
    private GameContext gameContext;
    private Player mockCurrentPlayer;
    private TurnManager mockTurnManager;
    private PlayerManager mockPlayerManager;
    private Deck mockDeck;
    private UserInterface userInterface;


    @BeforeEach
    public void setUp() {
        mockCurrentPlayer = EasyMock.createMock(Player.class);
        gameContext = new GameContext(mockCurrentPlayer);
        mockTurnManager = EasyMock.createMock(TurnManager.class);
        mockDeck = EasyMock.createMock(Deck.class);
        mockPlayerManager = EasyMock.createMock(PlayerManager.class);
        userInterface = EasyMock.createMock(UserInterface.class);
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
        Player result = gameContext.getCurrentPlayer();
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

    // TODO: Don't do anything, but just a placeholder for now
    @Test
    void endTurnWithoutDrawing_doesNotThrow() {
        assertDoesNotThrow(() -> gameContext.endTurnWithoutDrawing());
    }

    @Test
    void constructor_withValidParameters_createsGameContext() {
        GameContext fullGameContext = new GameContext(mockTurnManager, mockPlayerManager,
                mockDeck, mockCurrentPlayer, userInterface);
        assertNotNull(fullGameContext);
        assertEquals(mockCurrentPlayer, fullGameContext.getCurrentPlayer());
    }

    @Test
    void constructor_withNullTurnManager_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new GameContext(null, mockPlayerManager, mockDeck, mockCurrentPlayer,
                    userInterface);
        });
    }

    @Test
    void constructor_withNullPlayerManager_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new GameContext(mockTurnManager, null, mockDeck,
                    mockCurrentPlayer, userInterface);
        });
    }

    @Test
    void constructor_withNullDeck_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new GameContext(mockTurnManager, mockPlayerManager, null,
                    mockCurrentPlayer, userInterface);
        });
    }

    @Test
    void constructor_withNullCurrentPlayer_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new GameContext(mockTurnManager, mockPlayerManager, mockDeck,
                    null, userInterface);
        });
    }

    @Test
    void fullConstructor_withNullUI_allowsNullUI() {
        assertDoesNotThrow(() -> {
            new GameContext(mockTurnManager, mockPlayerManager, mockDeck, mockCurrentPlayer, null);
        });
    }

    @Test
    void addTurnForCurrentPlayer_withFullContext_callsTurnManager() {
        GameContext fullGameContext = new GameContext(mockTurnManager, mockPlayerManager,
                mockDeck, mockCurrentPlayer, userInterface);

        mockTurnManager.addTurnForCurrentPlayer();
        expectLastCall().once();
        replay(mockTurnManager);

        fullGameContext.addTurnForCurrentPlayer();

        verify(mockTurnManager);
    }

    @Test
    void addTurnForCurrentPlayer_withSimpleContext_doesNotThrow() {
        assertDoesNotThrow(() -> gameContext.addTurnForCurrentPlayer());
    }
}
