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

        List<Player> players = playerManager.getPlayers();
        if (players.isEmpty()) {
            throw new IllegalArgumentException("No players provided");
        }

        this.turnQueue.clear();
        this.turnQueue.addAll(players);
        this.currentPlayer = this.turnQueue.peek();

    }

    public void endTurnWithoutDraw() {
        if (turnQueue.isEmpty()) {
            throw new IllegalStateException("TurnManager not initialized");
        }

        currentPlayer.drawCard(deck);
        advanceToNextPlayer();
    }

    private void advanceToNextPlayer() {
        Player current = turnQueue.poll();
        turnQueue.offer(current);
        this.currentPlayer = turnQueue.peek();
    }

    public void syncWith(List<Player> activePlayers) {
        Objects.requireNonNull(activePlayers, "Active players list cannot be null");
        if (activePlayers.isEmpty()) {
            throw new IllegalArgumentException("No players provided");
        }

        this.turnQueue.clear();
        this.turnQueue.addAll(activePlayers);
    }

    public void addTurnForCurrentPlayer() {
        if (currentPlayer == null) {
            throw new IllegalStateException("TurnManager not initialized");
        }

        if (turnQueue.size() > 1) {
            LinkedList<Player> list = (LinkedList<Player>) turnQueue;
            list.add(1, currentPlayer);
        }
    }
}
