package domain;


import java.util.*;

import ui.UserInterface;

public class GameEngine {
	private final CardManager cardManager;
	private final TurnManager turnManager;
	private final PlayerManager playerManager;
	private final Deck deck;
	private final UserInterface userInterface;
	private final CardFactory cardFactory;

	public GameEngine(
			TurnManager turnManager,
			PlayerManager playerManager,
			Deck deck,
			UserInterface userInterface,
			CardFactory cardFactory
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
	}

	public void playCard(Player player, Card card) {
		Objects.requireNonNull(player, "Player cannot be null");
		Objects.requireNonNull(card, "Card cannot be null");

		GameContext gameContext = createGameContext(player);
		cardManager.playCard(card, player, gameContext);
	}

	private GameContext createGameContext(Player player) {
		return new GameContext(
				turnManager,
				playerManager,
				deck,
				player,
				userInterface
		);
	}

	public static List<Card> createInitialDeck(CardFactory cardFactory,
												int numberOfPlayers) {

        List<Card> deck = new ArrayList<>();
		deck.addAll(cardFactory.createCards(CardType.ATTACK, 4));
		deck.addAll(cardFactory.createCards(CardType.SKIP, 4));
		deck.addAll(cardFactory.createCards(CardType.FAVOR, 4));
		deck.addAll(cardFactory.createCards(CardType.SHUFFLE, 4));
		deck.addAll(cardFactory.createCards(CardType.SEE_THE_FUTURE,
				5));
		deck.addAll(cardFactory.createCards(CardType.ALTER_THE_FUTURE,
				4));
		deck.addAll(cardFactory.createCards(CardType.NUKE, 1));
		// We're giving the players two extra defuses in the deck
		deck.addAll(cardFactory.createCards(CardType.DEFUSE, 2));

		int currentCards = deck.size();
		int targetNumberOfCards = 56 - numberOfPlayers;
		int numberOfCardsNeeded = currentCards - targetNumberOfCards;

		if (numberOfCardsNeeded > 0) {
			deck.addAll(cardFactory.createCards(CardType.NORMAL,
					numberOfCardsNeeded));
		}

		return deck;
	}

	public static GameEngine createNewGame() {
		UserInterface userInterface = new UserInterface();
		CardFactory cardFactory = new CardFactory();

		userInterface.displayWelcome();
		int numberOfPlayers = userInterface.getNumberOfPlayers();

		List<Card> startingDeck = createInitialDeck(cardFactory,
				numberOfPlayers);

		Deck deck = new Deck(startingDeck);

		deck.shuffleDeck(new Random());

		PlayerManager playerManager = new PlayerManager(deck);
		TurnManager turnManager = new TurnManager(deck);

		playerManager.addPlayers(numberOfPlayers);
		turnManager.setPlayerManager(playerManager);

		return new GameEngine(turnManager, playerManager, deck, userInterface,
				cardFactory);
	}

	public void showAvailableCardTypes(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
		List<CardType> available = player.getAvailableCardTypes();
		if (!available.isEmpty()) {
			System.out.print("Available cards: ");
			for (int i = 0; i < available.size(); i++) {
				System.out.print(available.get(i)
						.name().toLowerCase()
						.replace("_", " "));
				if (i < available.size() - 1) System.out.print(", ");
			}
			System.out.println();
		}
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
}
