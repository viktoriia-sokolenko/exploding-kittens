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

}
