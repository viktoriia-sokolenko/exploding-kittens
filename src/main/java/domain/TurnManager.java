package domain;

import java.util.*;

public class TurnManager {
    private final Deck deck;
    private Queue<Player> turnQueue;
    private Player currentPlayer;

    public TurnManager(Deck deck) {
        this.deck = Objects.requireNonNull(deck, "Deck cannot be null");
        this.turnQueue = new LinkedList<>();
        this.currentPlayer = null;
    }

    public Player getCurrentActivePlayer() {
        if (currentPlayer == null) {
            throw new IllegalStateException("TurnManager not initialized");
        }
        return currentPlayer;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        Objects.requireNonNull(playerManager,
                "PlayerManager cannot be null");

        
    }
}
