package domain;

import ui.UserInterface;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
		int maxPlayerIndex = playerManager.getNumberOfPlayers() - 1;
		String playerMessage = "Enter the index of a player you want to get card from"
				+ " (0, " + maxPlayerIndex + ")";
		Player playerGiver = getPlayerFromUserInput(playerMessage, maxPlayerIndex);
		String cardMessage = "Enter card type you want to give to current player";
		Card cardToTransfer = getCardFromUserInput(cardMessage, playerGiver);
		playerGiver.removeCardFromHand(cardToTransfer);
		currentPlayer.addCardToHand(cardToTransfer);
	}

	public void viewTopTwoCardsFromDeck() {
		List<Card> topTwoCards = deck.peekTopTwoCards();
		int deckSize = deck.getDeckSize();
		userInterface.displayCardsFromDeck(topTwoCards, deckSize);
	}

	public void shuffleDeckFromDeck() {
		Random random = new SecureRandom();
		deck.shuffleDeck(random);
	}

	public void rearrangeTopThreeCardsFromDeck() {
		List<Card> topThreeCards = deck.peekTopThreeCards();
		userInterface.displayCardsFromDeck(topThreeCards,
				deck.getDeckSize());
		List<Integer> indices = getIndicesFromUserInput(topThreeCards);
		deck.rearrangeTopThreeCards(indices);
	}

	private Card getCardFromUserInput(String message, Player player) {
		String cardTypeInput = userInterface.getUserInput(message);
		CardType cardType = player.parseCardType(cardTypeInput);
		return cardFactory.createCard(cardType);
	}

	private Player getPlayerFromUserInput(String message, int maxPlayerIndex) {
		int playerIndex = userInterface.getNumericUserInput(message, 0, maxPlayerIndex);
		return playerManager.getPlayerByIndex(playerIndex);
	}

	private List<Integer> getIndicesFromUserInput(List<Card> topThreeCards) {
		int deckSize = deck.getDeckSize();

		final int cardsToRearrange = topThreeCards.size();
		int minCardIndex = deckSize - cardsToRearrange;
		int maxCardIndex = deckSize - 1;

		List<Integer> indices = new ArrayList<>();

		for (int i = 0; i < cardsToRearrange; i++) {
			String messageForPlayer =
					"Enter the index of a card " +
							"that you want to put in position " +
							i +
							" starting from the top of the Deck.\n" +
							"Only possible indices are from " +
							minCardIndex +
							" to " + maxCardIndex + "." +
							" Indices can not repeat.";
			indices.add(userInterface.getNumericUserInput
					(messageForPlayer, minCardIndex, maxCardIndex));
		}

		return indices;
	}
}
