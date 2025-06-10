package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;
import ui.UserInterface;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    private GameEngine gameEngine;
    private TurnManager mockTurnManager;
    private PlayerManager mockPlayerManager;
    private UserInterface mockUserInterface;
    private Deck mockDeck;

    @BeforeEach
    public void setUp() {
        mockTurnManager = EasyMock.createMock(TurnManager.class);
        mockPlayerManager = EasyMock.createMock(PlayerManager.class);
        mockUserInterface = EasyMock.createMock(UserInterface.class);
        mockDeck = EasyMock.createMock(Deck.class);

        gameEngine = new GameEngine(
                mockTurnManager,
                mockPlayerManager,
                mockDeck,
                mockUserInterface
        );
    }

    @Test
    public void constructor_withNullTurnManager_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> new GameEngine(
                        null,
                        mockPlayerManager,
                        mockDeck,
                        mockUserInterface
                )
        );
        assertEquals("turnManager must not be null",
                thrown.getMessage());
    }
}
