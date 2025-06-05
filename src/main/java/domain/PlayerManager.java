package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlayerManager {
	private final Deck deck;
	private final List<Player> players;
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 5;

	public PlayerManager(Deck deck) {
		this.deck = Objects.requireNonNull(deck, "Deck cannot be null");
		this.players = new ArrayList<>();
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
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
		if (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS) {
			throw new IllegalArgumentException(
					"Number of players must be between 2 and 5");
		}

		for (int i = 0; i < numberOfPlayers; i++) {
			Hand hand = new Hand();
			Player player = new Player(hand);
			players.add(player);
		}
	}

	public void removePlayerFromGame(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");

		if (!players.contains(player)) {
			throw new IllegalArgumentException("Player not found in game");
		}

		player.activeStatus = false;
	}

}
