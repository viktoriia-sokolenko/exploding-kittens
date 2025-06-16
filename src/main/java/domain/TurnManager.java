package domain;

import java.util.*;

public class TurnManager {
	private final Queue<Player> turnQueue;
	private int currentPlayerTurnsTaken;
	private int requiredTurns;
	private Player currentPlayer;

	public TurnManager() {
		this.turnQueue = new LinkedList<>();
		this.currentPlayer = null;
		this.requiredTurns = 0;
		this.currentPlayerTurnsTaken = 0;
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

		if (isUnderAttack()) {
			incrementTurnsTaken();
		} else {
			advanceToNextPlayer();
		}
	}

	public void endTurnWithoutDrawForAttacks() {
		if (turnQueue.isEmpty()) {
			throw new IllegalStateException("No players to manage");
		}
		int remainingTurns = requiredTurns - currentPlayerTurnsTaken;
		advanceToNextPlayer();
		requiredTurns = remainingTurns + 2;
		currentPlayerTurnsTaken = 0;
	}

	public void advanceToNextPlayer() {
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

	public void reverseOrder() {
		if (turnQueue.isEmpty()) {
			throw new IllegalStateException("No players to manage");
		}
		List<Player> players = new ArrayList<>(turnQueue);
		Collections.reverse(players);
		syncWith(players);
	}

	public void incrementTurnsTaken() {
		currentPlayerTurnsTaken++;
		if (currentPlayerTurnsTaken >= requiredTurns) {
			advanceToNextPlayer();
			requiredTurns = 1;
			currentPlayerTurnsTaken = 0;
		}
	}

	public boolean isUnderAttack() {
		return requiredTurns > 1 && currentPlayerTurnsTaken < requiredTurns;
	}

	public void setRequiredTurns(int requiredTurns) {
		if (requiredTurns < 0) {
			throw new IllegalArgumentException("Required turns cannot be negative");
		}
		this.requiredTurns = requiredTurns;
	}

	public void setCurrentPlayerTurnsTaken(int currentPlayerTurnsTaken) {
		if (currentPlayerTurnsTaken < 0) {
			throw new IllegalArgumentException(
					"Current player turns taken cannot be negative"
			);
		}
		this.currentPlayerTurnsTaken = currentPlayerTurnsTaken;
	}

	public int getRequiredTurns() {
		return requiredTurns;
	}

	public int getCurrentPlayerTurnsTaken() {
		return currentPlayerTurnsTaken;
	}
}
