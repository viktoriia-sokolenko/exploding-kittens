package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class TurnManagerTest {

    private TurnManager turnManager;
    private Deck deck;
    private PlayerManager playerManager;

    @BeforeEach
    void setUp() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            cards.add(new SkipCard()); // Placeholder for since this is only
            // card that's basically implemented
        }
        deck = new Deck(cards);
        turnManager = new TurnManager(deck);

        playerManager = new PlayerManager(deck);
        playerManager.addPlayers(3);
    }

    @Test
    void constructor_withNullDeck_throwsNullPointerException() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new TurnManager(null)
        );

        assertEquals("Deck cannot be null", exception.getMessage());
    }
}
