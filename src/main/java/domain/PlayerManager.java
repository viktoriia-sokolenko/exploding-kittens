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

    public void addPlayers(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 5) {
            throw new IllegalArgumentException("Number of players must be between 2 and 5");
        }
    }

}
