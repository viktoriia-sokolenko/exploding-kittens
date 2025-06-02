package domain;

import java.util.ArrayList;
import java.util.List;

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
        List<Player> active = new ArrayList<>();
        for (Player p : players) {
            if (p.isInGame()) {
                active.add(p);
            }
        }
        return active;
    }

    public void addPlayers(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 5) {
            throw new IllegalArgumentException("Number of players must be between 2 and 5");
        }

        for (int i = 0; i < numberOfPlayers; i++) {
            Hand hand = new Hand();
            Player player = new Player(hand);
            players.add(player);
        }
    }
}
