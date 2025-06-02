package domain;

import java.util.*;

public class PlayerManager {
    private final Deck deck;
    private final List<Player> players;

    public PlayerManager(Deck deck) {
        if (deck == null) {
            throw new NullPointerException("Deck cannot be null");
        }
        this.deck = deck;
        this.players = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getActivePlayers() {
        return new ArrayList<>();
    }

}
