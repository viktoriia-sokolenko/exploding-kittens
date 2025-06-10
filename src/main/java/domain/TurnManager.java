package domain;

import java.util.*;

public class TurnManager {
	private final Deck deck;
	private final Queue<Player> turnQueue;
	private Player currentPlayer;

	public TurnManager(Deck deck) {
		this.deck = Objects.requireNonNull(deck, "Deck cannot be null");
		this.turnQueue = new LinkedList<>();
		this.currentPlayer = null;
	}

	Player getCurrentActivePlayer() {
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
			throw new IllegalStateException("No players to manage");
		}

		advanceToNextPlayer();
	}

	public void endTurnWithoutDrawForAttacks() {
		if (turnQueue.isEmpty()) {
			throw new IllegalStateException("No players to manage");
		}

		this.endTurnWithoutDraw();
		this.addTurnForCurrentPlayer();
		this.addTurnForCurrentPlayer();
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
		this.currentPlayer = this.turnQueue.peek();
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

	public List<Player> getTurnOrder() {
		if (turnQueue.isEmpty()) {
			throw new IllegalStateException("TurnManager not initialized");
		}

		return new ArrayList<>(turnQueue);
	}

	public int getTurnsFor(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
		int count = 0;
		for (Player queuePlayer : turnQueue) {
			if (player.equals(queuePlayer)) {
				count++;
			}
		}
		return count;
	}
}
