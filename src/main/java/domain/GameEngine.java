package domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

	public static GameEngine createNewGame() {
		UserInterface userInterface = new UserInterface();
		CardFactory cardFactory = new CardFactory();

		userInterface.displayWelcome();
		int numberOfPlayers = userInterface.getNumberOfPlayers();

		// TODO: this needs to be an actual array of cards
		// will create a method to implement this
		List<Card> startingDeck = new ArrayList<>();

		Deck deck = new Deck(startingDeck);

		deck.shuffleDeck(new Random());

		PlayerManager playerManager = new PlayerManager(deck);
		TurnManager turnManager = new TurnManager(deck);

		playerManager.addPlayers(numberOfPlayers);
		turnManager.setPlayerManager(playerManager);

		return new GameEngine(turnManager, playerManager, deck, userInterface,
				cardFactory);
	}
}
