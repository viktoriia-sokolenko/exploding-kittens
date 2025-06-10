package domain;


import java.util.Objects;
import ui.UserInterface;

public class GameEngine {
    private final CardManager cardManager;
    private final TurnManager turnManager;
    private final PlayerManager playerManager;
    private final Deck deck;
    private final UserInterface userInterface;

    public GameEngine(
            TurnManager turnManager,
            PlayerManager playerManager,
            Deck deck,
            UserInterface userInterface
    ) {
        this.cardManager   = new CardManager();
        this.turnManager   = Objects.requireNonNull(turnManager,
                "turnManager must not be null");
        this.playerManager = Objects.requireNonNull(playerManager,
                "playerManager must not be null");
        this.deck          = Objects.requireNonNull(deck,
                "deck must not be null");
        this.userInterface = userInterface;
    }


}
