package domain;

import ui.UserInterface;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameContext {
	private final TurnManager turnManager;
	private final PlayerManager playerManager;
	private final Deck deck;
	private final Player currentPlayer;
	private final UserInterface userInterface;

	public GameContext(TurnManager turnManager, PlayerManager playerManager,
						Deck deck, Player currentPlayer, UserInterface
								userInterface) {
		this.turnManager = Objects.requireNonNull(turnManager,
				"TurnManager cannot be null");
		this.playerManager = Objects.requireNonNull(playerManager,
				"PlayerManager cannot be null");
		this.deck = Objects.requireNonNull(deck,
				"Deck cannot be null");
		this.currentPlayer = Objects.requireNonNull(currentPlayer,
				"Current player cannot be null");
		this.userInterface = userInterface;
	}

	public GameContext(Player currentPlayer) {
		this.currentPlayer = Objects.requireNonNull(currentPlayer,
				"Current player cannot be null");
		this.turnManager = null;
		this.playerManager = null;
		this.deck = null;
		this.userInterface = null;
	}

	Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void endTurnWithoutDrawing() {
		if (turnManager != null) {
			turnManager.endTurnWithoutDraw();
		}
	}

	public void addTurnForCurrentPlayer() {
		if (turnManager != null) {
			turnManager.addTurnForCurrentPlayer();
		}
	}

	public List<Card> viewTopTwoCardsFromDeck() {
		return deck.peekTopTwoCards();
	}

	public void shuffleDeckFromDeck() {
		Random random = new SecureRandom();
		deck.shuffleDeck(random);
	}
}
