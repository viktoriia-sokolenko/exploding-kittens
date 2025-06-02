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
}
