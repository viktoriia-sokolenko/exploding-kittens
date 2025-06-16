package domain;


import java.util.*;
import java.security.SecureRandom;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import locale.LocaleManager;
import ui.UserInterface;

public class GameEngine {
	private final CardManager cardManager;
	private final TurnManager turnManager;
	private final PlayerManager playerManager;
	private final Deck deck;
	private final UserInterface userInterface;
	private final CardFactory cardFactory;
	private final SecureRandom secureRandom;
	private boolean gameRunning = true;
	private final LocaleManager localeManager;

	public GameEngine(
			TurnManager turnManager,
			PlayerManager playerManager,
			Deck deck,
			UserInterface userInterface,
			CardFactory cardFactory,
			SecureRandom secureRandom,
			LocaleManager localeManager
	) {
		this.cardManager   = new CardManager();
		this.turnManager   = Objects.requireNonNull(turnManager,
				"turnManager must not be null");
		this.playerManager = Objects.requireNonNull(playerManager,
				"playerManager must not be null");
		this.deck		   = Objects.requireNonNull(deck,
				"deck must not be null");
		this.userInterface = userInterface;
		this.cardFactory = Objects.requireNonNull(cardFactory,
				"cardFactory must not be null");
		// We need this in order to satisfy the spotsbug error
		this.secureRandom = new SecureRandom();
		this.localeManager = localeManager;
	}

	public void playCard(Player player, Card card) {
		Objects.requireNonNull(player, "Player cannot be null");
		Objects.requireNonNull(card, "Card cannot be null");

		GameContext gameContext = createGameContext(player);
		cardManager.playCard(card, player, gameContext);
	}

	public void initializeGame() {
		List<Player> players = playerManager.getPlayers();
		final int NUMBER_OF_STARTING_CARDS = 4;
		int explodingKittensCount = players.size() - 1;

		initializeHandPerPlayers(players, NUMBER_OF_STARTING_CARDS);
		insertExplodingKittenPerPlayers(explodingKittensCount);

		gameRunning = true;
		userInterface.displayHelp();
	}

	public void runGameLoop() {
		while (gameRunning) {
			Player currentPlayer = turnManager.getCurrentActivePlayer();

			if (!currentPlayer.isInGame()) {
				handlePlayerGetsEliminated();
				continue;
			}

			displayGameState(currentPlayer);

			String input = userInterface.getUserInput();
			processCommand(input, currentPlayer);

			checkWinCondition();
		}
	}

	public boolean getIsGameRunning() {
		return this.gameRunning;
	}

	public void setGameRunning(boolean shouldStopGame) {
		this.gameRunning = shouldStopGame;
	}

	public void handleQuitCommand() {
		String quitMessage = getMessage("game.quit.thanks");
		System.out.println(quitMessage);
		setGameRunning(false);
	}

	public static List<Card> createInitialDeck
			(CardFactory cardFactory, int numberOfPlayers) {

		List<Card> deck = new ArrayList<>();
		final int NUMBER_OF_ESSENTIAL_CARDS = 4;
		final int NUMBER_OF_NUKE_CARDS = 1;
		final int NUMBER_OF_EXTRA_DEFUSE_CARDS = 2;
		final int NUMBER_OF_SEE_THE_CARDS = 5;
		deck.addAll(cardFactory
				.createCards(CardType.ATTACK, NUMBER_OF_ESSENTIAL_CARDS));
		deck.addAll(cardFactory
				.createCards(CardType.SKIP, NUMBER_OF_ESSENTIAL_CARDS));
		deck.addAll(cardFactory
				.createCards(CardType.FAVOR, NUMBER_OF_ESSENTIAL_CARDS));
		deck.addAll(cardFactory
				.createCards(CardType.SHUFFLE, NUMBER_OF_ESSENTIAL_CARDS));
		deck.addAll(cardFactory
				.createCards(CardType.SEE_THE_FUTURE,
				NUMBER_OF_SEE_THE_CARDS));
		deck.addAll(cardFactory
				.createCards(CardType.ALTER_THE_FUTURE,
				NUMBER_OF_ESSENTIAL_CARDS));
		deck.addAll(cardFactory
				.createCards(CardType.NUKE, NUMBER_OF_NUKE_CARDS));
		// We're giving the players two extra defusing in the deck
		deck.addAll(cardFactory
				.createCards
						(CardType.DEFUSE, NUMBER_OF_EXTRA_DEFUSE_CARDS));

		int currentCards = deck.size();
		final int TARGET_NUMBER_OF_CARDS = 56;
		int targetNumberOfCards = TARGET_NUMBER_OF_CARDS - numberOfPlayers;
		int numberOfCardsNeeded = currentCards - targetNumberOfCards;

		if (numberOfCardsNeeded > 0) {
			deck.addAll(cardFactory.createCards(CardType.NORMAL,
					numberOfCardsNeeded));
		}

		return deck;
	}

	public static GameEngine createNewGame() {
		LocaleManager localeManager = new LocaleManager();

		UserInterface userInterface = new UserInterface(localeManager);

		String localeMessage = localeManager.get("choose.locale");
		int locale = userInterface.getNumericUserInput(localeMessage, 1, 2);
		localeManager.chooseLocale(locale);

		CardFactory cardFactory = new CardFactory();

		userInterface.displayWelcome();
		int numberOfPlayers = userInterface.getNumberOfPlayers();

		List<Card> startingDeck = createInitialDeck(cardFactory,
				numberOfPlayers);

		// We need this in order to satisfy the spotsbug error
		SecureRandom secureRandom = new SecureRandom();
		Deck deck = new Deck(startingDeck);
		deck.shuffleDeck(secureRandom);

		PlayerManager playerManager = new PlayerManager(deck);
		TurnManager turnManager = new TurnManager(deck);
		playerManager.addPlayers(numberOfPlayers);
		turnManager.setPlayerManager(playerManager);

		return new GameEngine(turnManager, playerManager, deck, userInterface,
				cardFactory, secureRandom, localeManager);
	}

	public void showAvailableCardTypes(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
		List<CardType> available = player.getAvailableCardTypes();
		if (!available.isEmpty()) {

			System.out.print(getMessage("game.available.cards"));
			for (int i = 0; i < available.size(); i++) {
				System.out.print(available.get(i)
						.name().toLowerCase()
						.replace("_", " "));
				if (i < available.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}
	}

	public void displayGameState(Player currentPlayer) {
		final int NUMBER_OF_EQUAL_SIGNS = 40;
		System.out.println("\n" + "=".repeat(NUMBER_OF_EQUAL_SIGNS));
		int currentPlayerIndex = playerManager.getPlayers().indexOf(currentPlayer);
		System.out.println("Turn of player " + currentPlayerIndex);
		System.out.println("=".repeat(NUMBER_OF_EQUAL_SIGNS));
		System.out.println("Players remaining: " + playerManager.getActivePlayers().size());
		displayIndexesOfActivePlayers();
		System.out.println("Cards in deck: " + deck.getDeckSize());
		userInterface.displayPlayerHand(currentPlayer);
	}

	public void displayGameStatus() {
		System.out.println("\n=== GAME STATUS ===");
		List<Player> activePlayers = playerManager.getActivePlayers();
		System.out.println("Active players: " + activePlayers.size());
		System.out.println("Cards in deck: " + deck.getDeckSize());
		Player current = turnManager.getCurrentActivePlayer();
		System.out.println("Current player has " + current.getNumberOfCards()
				+ " cards");
		System.out.println("==================\n");
	}


	public void handlePlayCommand(String[] parts, Player currentPlayer) {
		Objects.requireNonNull(parts, "Player cannot be null");
		Objects.requireNonNull(currentPlayer, "Card cannot be null");

		final int MINIMUM_NUMBER_OF_PARTS = 2;
		if (parts.length < MINIMUM_NUMBER_OF_PARTS) {
			userInterface.displayError
					("Usage: play <card_type> " +
							"(e.g., 'play skip'" +
							" or 'play attack')");
			return;
		}

		String cardTypeName = String.join(" ", Arrays.copyOfRange(parts,
				1, parts.length));
		CardType cardTypeToPlay = currentPlayer.parseCardType(cardTypeName);

		Card cardToPlay = cardFactory.createCard(cardTypeToPlay);
		playCard(currentPlayer, cardToPlay);
		userInterface.displayCardPlayed(cardToPlay);
	}

	public void handleDrawCommand(Player currentPlayer) {
		Objects.requireNonNull(currentPlayer, "Player cannot be null");

		final int DECK_SIZE_OF_ZERO = 0;
		if (deck.getDeckSize() == DECK_SIZE_OF_ZERO) {
			userInterface.displayError("Deck is empty!");
			return;
		}

		Card drawnCard = deck.draw();
		userInterface.displayDrawnCard(drawnCard);

		if (drawnCard.getCardType() == CardType.EXPLODING_KITTEN) {
			handleExplodingKittenDrawWithUI(currentPlayer, drawnCard);
		} else {
			currentPlayer.addCardToHand(drawnCard);
			turnManager.advanceToNextPlayer();
		}
	}

	private void handleExplodingKittenDrawWithUI
			(Player currentPlayer, Card explodingKitten) {
		if (currentPlayer.hasCardType(CardType.DEFUSE)) {
			currentPlayer.removeDefuseCard();
			userInterface.displayDefuseUsed();
			int position = getPlayerChoiceForKittenPlacement();
			deck.insertCardAt(explodingKitten, position);

			userInterface.displaySuccess("Exploding Kitten placed" +
					" back in the deck at position " + position);
			turnManager.advanceToNextPlayer();
		} else {
			System.out.println("BOOM! You drew an Exploding Kitten and " +
					"had no Defuse card!");
			playerManager.removePlayerFromGame(currentPlayer);
		}
	}

	public int getPlayerChoiceForKittenPlacement() {
		int deckSize = deck.getDeckSize();
		String message = "Choose a position to insert the Exploding Kitten " +
				"(0 = bottom, "
				+ deckSize + " = top of deck)";
		final int MIN = 0;
		return userInterface.getNumericUserInput(message, MIN, deckSize);
	}

	public void processCommand(String input, Player currentPlayer) {
		if (input == null || input.trim().isEmpty()) {
			userInterface.
					displayError("Please enter a command. " +
							"Type 'help' for available commands.");
			return;
		}
		String cleanedInput	 = input.trim().replaceAll("\\s+", " ");
		String[] parts = cleanedInput.split(" ");
		String command = parts[0];

		try {
			switch (command) {
				case "play":
					handlePlayCommand(parts, currentPlayer);
					break;
				case "help":
					userInterface.displayHelp();
					break;
				case "hand":
					userInterface.displayPlayerHand(currentPlayer);
					break;
				case "draw":
					handleDrawCommand(currentPlayer);
					break;
				case "status":
					displayGameStatus();
					break;
				case "quit":
					handleQuitCommand();
					break;
				default:
					userInterface
							.displayError
							("Unknown command: " + command	+
									". " +
									"Type 'help' " +
									"for available commands.");
			}
		} catch (Exception e) {
			userInterface.displayError("Error executing command: " +
					e.getMessage());
		}

	}


	public void handlePlayerGetsEliminated() {
		List<Player> activePlayers = playerManager.getActivePlayers();
		turnManager.syncWith(activePlayers);
	}

	public void checkWinCondition() {
		List<Player> activePlayers = playerManager.getActivePlayers();
		if (activePlayers.size() <= 1) {
			gameRunning = false;
			if (activePlayers.size() == 1) {
				System.out.println("\nGAME OVER! The last player standing wins!");
			} else {
				System.out.println("\nGAME OVER! Everyone exploded!");
			}
		}
	}

	private GameContext createGameContext(Player player) {
		return new GameContext(
				turnManager,
				playerManager,
				deck,
				player,
				userInterface,
				cardFactory
		);
	}

	private void initializeHandPerPlayers(List<Player> players, int startingCardCount) {
		for (Player player : players) {
			for (int i = 0; i < startingCardCount; i++) {
				player.drawCard(deck);
			}

			player.addCardToHand(cardFactory.createCard(CardType.DEFUSE));
		}
	}

	private void insertExplodingKittenPerPlayers(int explodingKittensCount) {
		for (int i = 0; i < explodingKittensCount; i++) {
			deck.insertCardAt(cardFactory.createCard(CardType.EXPLODING_KITTEN),
					secureRandom.nextInt(deck.getDeckSize()));
		}
	}

	private void displayIndexesOfActivePlayers() {
		List<Player> allPlayers = playerManager.getPlayers();
		List<Integer> activePlayerIndexes = new ArrayList<>();
		for (int i = 0; i < allPlayers.size(); i++) {
			if (allPlayers.get(i).isInGame()) {
				activePlayerIndexes.add(i);
			}
		}
		System.out.println("Active players indices: " + activePlayerIndexes);
	}

	private String getMessage(String key) {
		return localeManager.get(key);
	}

	public static void main(String[] args) {
		GameEngine game = createNewGame();
		game.initializeGame();
		game.runGameLoop();
	}
}
