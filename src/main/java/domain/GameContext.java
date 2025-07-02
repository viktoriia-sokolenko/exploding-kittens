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

		String playerMessage = userInterface.getPlayerIndexPrompt(maxPlayerIndex);
		Player playerGiver = getPlayerFromUserInput(playerMessage, maxPlayerIndex);
		String cardMessage = userInterface.getCardTransferPrompt();
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

	public void reverseOrderPreservingAttackState() {
		if (turnManager != null) {
			if (turnManager.isUnderAttack()) {
				turnManager.incrementTurnsTaken();
			}
			turnManager.reverseOrder();
		}
	}

	public void rearrangeTopThreeCardsFromDeck() {
		List<Card> topThreeCards = deck.peekTopThreeCards();
		userInterface.displayCardsFromDeck(topThreeCards,
				deck.getDeckSize());
		List<Integer> indices = getIndicesFromUserInput(topThreeCards);
		deck.rearrangeTopThreeCards(indices);
	}

	public void swapTopAndBottomDeckCards() {
		deck.swapTopAndBottom();
	}

	public void buryCardImplementation() {
		Card topCard = deck.draw();
		userInterface.displayDrawnCard(topCard);

		int deckSize = deck.getDeckSize();
		int insertIndex = getBuryCardIndexFromUserInput(deckSize);

		deck.insertCardAt(topCard, insertIndex);
		endTurnWithoutDrawing();
	}

	public void moveAllExplodingKittensToTop() {
		if (deck != null && userInterface != null) {
			deck.moveAllExplodingKittensToTop();
		}
	}

	private Card getCardFromUserInput(String message, Player player) {
		userInterface.displayPlayerHand(player);
		String cardTypeInput = userInterface.getUserInput(message);
		CardType cardType = player.parseCardType(cardTypeInput);
		if (cardType == null) {
			throw new IllegalArgumentException(
					"Inputted card type is invalid or was not found in Hand");
		}
		return cardFactory.createCard(cardType);
	}

	private Player getPlayerFromUserInput(String message, int maxPlayerIndex) {
		int playerIndex = userInterface.getNumericUserInput(message, 0, maxPlayerIndex);
		userInterface.displayPlayerChangeMessage(playerIndex);
		return playerManager.getPlayerByIndex(playerIndex);

	}

	private int getBuryCardIndexFromUserInput(int deckSize) {
		String message = userInterface.getBuryCardPrompt(deckSize);
		return userInterface.getNumericUserInput(message, 0, deckSize);
	}

	private List<Integer> getIndicesFromUserInput(List<Card> topThreeCards) {
		int deckSize = deck.getDeckSize();

		final int cardsToRearrange = topThreeCards.size();
		int minCardIndex = deckSize - cardsToRearrange;
		int maxCardIndex = deckSize - 1;

		List<Integer> indices = new ArrayList<>();

		for (int i = 0; i < cardsToRearrange; i++) {
			String messageForPlayer = userInterface
					.getRearrangePrompt(i, minCardIndex, maxCardIndex);
			indices.add(userInterface.getNumericUserInput
					(messageForPlayer, minCardIndex, maxCardIndex));
		}

		return indices;
	}

}
