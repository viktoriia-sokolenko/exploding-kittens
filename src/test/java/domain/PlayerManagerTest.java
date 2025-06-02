package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class PlayerManagerTest {
    private PlayerManager playerManager;
    private Deck mockDeck;

    @BeforeEach
    void setUp() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            cards.add(new SkipCard()); // NOTE: adding skip cards because
            // that's the only that is semi-completed
        }
        mockDeck = new Deck(cards);
        playerManager = new PlayerManager(mockDeck);
    }

    @Test
    void constructor_withNullDeck_throwsNullPointerException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PlayerManager(null)
        );

        assertEquals("Deck cannot be null", exception.getMessage());
    }

    @Test
    void constructor_withValidDeck_initializesEmptyManager() {
        PlayerManager pm = new PlayerManager(mockDeck);

        assertEquals(0, pm.getPlayers().size());
        assertEquals(0, pm.getActivePlayers().size());
    }
}
