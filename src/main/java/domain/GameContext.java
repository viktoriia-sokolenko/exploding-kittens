package domain;

import ui.UserInterface;

import java.util.List;
import java.util.Objects;

public class GameContext {
	private final TurnManager turnManager;
	private final PlayerManager playerManager;
	private final Deck deck;
	private final Player currentPlayer;
	private final UserInterface userInterface;
	private final CardFactory cardFactory;

	public GameContext(TurnManager turnManager, PlayerManager playerManager,
		Deck deck, Player currentPlayer,
		UserInterface userInterface, CardFactory cardFactory) {
		this.turnManager = Objects.requireNonNull(turnManager,
				"TurnManager cannot be null");
		this.playerManager = Objects.requireNonNull(playerManager,
				"PlayerManager cannot be null");
		this.deck = Objects.requireNonNull(deck,
				"Deck cannot be null");
		this.currentPlayer = Objects.requireNonNull(currentPlayer,
				"Current player cannot be null");
		this.userInterface = userInterface;
		this.cardFactory = Objects.requireNonNull(cardFactory);
	}

	public GameContext(Player currentPlayer) {
		this.currentPlayer = Objects.requireNonNull(currentPlayer,
				"Current player cannot be null");
		this.turnManager = null;
		this.playerManager = null;
		this.deck = null;
		this.userInterface = null;
		this.cardFactory = null;
	}

	Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void endTurnWithoutDrawing() {
		if (turnManager != null) {
			turnManager.endTurnWithoutDraw();
		}
	}

	public void endTurnWithoutDrawingForAttacks() {
		if (turnManager != null) {
			turnManager.endTurnWithoutDrawForAttacks();
		}
	}

	public void addTurnForCurrentPlayer() {
		if (turnManager != null) {
			turnManager.addTurnForCurrentPlayer();
		}
	}

	public void transferCardBetweenPlayers() {
		String playerMessage = "Enter the player you want to get card from";
		Player playerGiver = getPlayerFromUserInput(playerMessage);
		String cardMessage = "Enter card type you want to give to current player";
		Card cardToTransfer = getCardFromUserInput(cardMessage, playerGiver);
		playerGiver.removeCardFromHand(cardToTransfer);
		currentPlayer.addCardToHand(cardToTransfer);
	}

	public List<Card> viewTopTwoCardsFromDeck() {
		return deck.peekTopTwoCards();
	}

	private Card getCardFromUserInput(String message, Player player) {
		String cardTypeInput = userInterface.getUserInput(message);
		CardType cardType = player.parseCardType(cardTypeInput);
		return cardFactory.createCard(cardType);
	}

	private Player getPlayerFromUserInput(String message) {
		int playerIndex = userInterface.getNumericUserInput(message);
		return playerManager.getPlayerByIndex(playerIndex);
	}
}
