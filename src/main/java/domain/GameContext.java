package domain;

import java.util.Objects;

public class GameContext {
    private final Player currentPlayer;

    public GameContext(Player currentPlayer) {
        this.currentPlayer = Objects.requireNonNull(currentPlayer, "Current player cannot be null");
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void playCardFromCurrentPlayerHand(Card card) {
        currentPlayer.playCard(card);
    }

    public void endTurnWithoutDrawing() {

    }
}
