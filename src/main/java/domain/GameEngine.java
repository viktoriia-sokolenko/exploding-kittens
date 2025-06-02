package domain;

import java.util.Objects;

public class GameEngine {
    private CardManager cardManager;

    public GameEngine() {
        this.cardManager = new CardManager();
    }

    public void playCard(Player player, Card card) {
        Objects.requireNonNull(player, "Player cannot be null");
    }
}
