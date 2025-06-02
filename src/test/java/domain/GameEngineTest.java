package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
    }

    @Test
    void playCard_withNullPlayer_throwsNullPointerException() {
        SkipCard skipCard = new SkipCard();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> gameEngine.playCard(null, skipCard)
        );

        assertEquals("Player cannot be null", exception.getMessage());
    }
}
