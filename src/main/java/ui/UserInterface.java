package ui;


import domain.CardType;
import domain.Player;
import domain.Card;
import locale.LocaleManager;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
	private final Scanner scanner;
	private static final int MAX_NUMBER_OF_PLAYERS = 5;
	private static final int MIN_NUMBER_OF_PLAYERS = 2;
	private LocaleManager localeManager;

	public UserInterface(LocaleManager localeManager) {
		this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
		this.localeManager = localeManager;
	}

	public void displayWelcome() {
		final String border = "=================================";
		System.out.println(border);
		System.out.println(getMessage("exploding.kittens"));
		System.out.println(border + "\n");
	}

	public void displayHelp() {
		System.out.println();
		System.out.println(getMessage("commands.available"));
		System.out.println(getMessage("command.play"));
		System.out.println(getMessage("command.draw"));
		System.out.println(getMessage("command.hand"));
		System.out.println(getMessage("command.status"));
		System.out.println(getMessage("command.help"));
		System.out.println(getMessage("command.quit"));
		System.out.println();
	}

	public void displayError(String message) {
		System.err.println(getMessage("error") + message);
	}

	public void displaySuccess(String message) {
		System.out.println(getMessage("success") + message);
	}

	public void displayWarning(String message) {
		System.err.println(getMessage("warning") + message);
	}

	public String getUserInput() {
		System.out.print("> ");
		return scanner.nextLine();
	}

	public int getNumberOfPlayers() {
		while (true) {
			System.out.print(getMessage("how.many.players"));
			String input = scanner.nextLine();
			try {
				int numberOfPlayers = Integer.parseInt(input);
				if (numberOfPlayers >= MIN_NUMBER_OF_PLAYERS &&
						numberOfPlayers <= MAX_NUMBER_OF_PLAYERS) {
					return numberOfPlayers;
				} else {
					displayError(getMessage("error.players.number"));
				}
			} catch (NumberFormatException ignored) {
				displayError(getMessage("error.players.number"));
			}
		}

	}

	public void displayPlayerHand(Player player) {
		int total = player.getNumberOfCards();

		final int NUMBER_OF_DASHES = 40;
		final int NO_CARDS = 0;
		System.out.println("\n" + "─".repeat(NUMBER_OF_DASHES));
		System.out.println(getMessage("hand.title") +
				" (" + total + " " +
				getMessage("hand.cards") + "):");
		System.out.println("─".repeat(NUMBER_OF_DASHES));

		if (total == NO_CARDS) {
			System.out.println("  " + getMessage("hand.empty"));
		} else {
			for (CardType type : CardType.values()) {
				Integer countInteger = player.getCardTypeCount(type);
				int count = (countInteger != null) ? countInteger : 0;

				if (count > 0) {
					String cardDisplay = formatCardName(type);
					String typeName = type.name().toLowerCase()
							.replace("_", " ");
					if (count == 1) {
						System.out.printf("	 %s (%s: %s)%n",
								cardDisplay,
								getMessage("hand.type"),
								typeName);
					} else {
						System.out.printf("	 %s x%d (%s: %s)%n",
								cardDisplay,
								count,
								getMessage("hand.type"),
								typeName);
					}
				}
			}
		}

		System.out.println("─".repeat(NUMBER_OF_DASHES));
	}

	public void displayInstructions() {
		final int NUMBER_OF_DASHES = 40;
		System.out.println(getMessage("hand.usage"));
		System.out.println("─".repeat(NUMBER_OF_DASHES) + "\n");
	}


	public void displayCardPlayed(Card card) {
		System.out.println(getMessage("card.played")
				+ formatCardName(card.getCardType())
				+ "\n");
		displayCardEffect(card.getCardType());
		System.out.println();
	}

	public void displayDrawnCard(Card card) {
		if (card.getCardType() == CardType.EXPLODING_KITTEN) {
			System.out.println(
					getMessage("card.drawn.exploding") +
					formatCardName(card.getCardType()));
		}

		System.out.println(getMessage("card.drawn") +
				formatCardName(card.getCardType()));
		System.out.println();
	}

	public void displayCardEffect(CardType cardType) {
		String key = "card.effect." + cardType.name().toLowerCase();
		String effect = getMessage(key);
		if (!effect.isBlank()) {
			System.out.println("   " + effect);
		}
	}

	public String formatCardName(CardType cardType) {
		String key = "card.name." + cardType.name().toLowerCase();
		if (getMessage(key).isBlank()) {
			return cardType.toString();
		};
		return getMessage(key);
	}

	public void displayTurnStart(int playerNumber, int totalPlayers) {
		String message = getMessage("turn.start");
		System.out.println("\n" +
				String.format(message, playerNumber, playerNumber, totalPlayers));
	}

	public void displayDeckEmpty() {
		displayWarning(getMessage("deck.empty"));
	}

	public void displayDefuseUsed() {
		System.out.println(getMessage("card.defuse.used"));
		System.out.println(getMessage("card.defuse.place.kitten"));
	}


	public void displayPlayerEliminated() {
		System.out.println(getMessage("player.eliminated"));
		System.out.println(getMessage("better.luck.wishes"));
	}

	public void displayGameEnd(boolean isThereGameWinner) {
		final int NUMBER_OF_EQUAL_SIGNS = 50;
		System.out.println("\n" + "=".repeat(NUMBER_OF_EQUAL_SIGNS));
		if (isThereGameWinner) {
			System.out.println(getMessage("game.win"));
			System.out.println(getMessage("game.win.survive.exploding"));
		} else {
			System.out.println(getMessage("game.lose"));
			System.out.println(getMessage("game.lose.exploded"));

		}

		System.out.println("=".repeat(NUMBER_OF_EQUAL_SIGNS));
		System.out.println(getMessage("game.quit.thanks"));
		System.out.println("=".repeat(NUMBER_OF_EQUAL_SIGNS) + "\n");
	}

	public String getUserInput(String message) {
		while (true) {
			System.out.println(message);
			System.out.print("> ");
			String input = scanner.nextLine();
			if (!input.isEmpty()) {
				return input;
			}
		}
	}

	public int getNumericUserInput(String message, int min, int max) {
		while (true) {
			System.out.println(message);
			System.out.print("> ");
			String input = scanner.nextLine();
			String errorMessage = String.format(
					getMessage("error.limit.number"), min, max
			);
			try {
				int inputInt = Integer.parseInt(input);
				if (inputInt < min || inputInt > max) {
					displayError(errorMessage);
				} else {
					return inputInt;
				}
			} catch (NumberFormatException e) {
				displayError(errorMessage);
			}
		}
	}

	public void displayCardsFromDeck(List<Card> cards, int deckSize) {
		if (deckSize < 0) {
			throw new IllegalArgumentException ("deckSize can not be negative");
		}
		if (cards.size() > deckSize) {
			throw new IllegalArgumentException(
					"deckSize is less than number of cards to display");
		}
		if (cards.isEmpty()) {
			System.out.println(getMessage("no.cards.view"));
			return;
		}
		System.out.println("\n" + getMessage("deck.view.top"));
		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			CardType cardTypeToDisplay = card.getCardType();
			int index = deckSize - i - 1;
			System.out.println(String.format(
					getMessage("deck.view.entry"),
					formatCardName(cardTypeToDisplay), index));
		}
	}

	public String getRearrangePrompt(int position, int minIndex, int maxIndex) {
		String format = getMessage("rearrange.card.prompt");
		return String.format(format, position, minIndex, maxIndex);
	}

	public String getPlayerIndexPrompt(int maxPlayerIndex) {
		String format = localeManager.get("player.index.prompt");
		return String.format(format, maxPlayerIndex);
	}

	public String getCardTransferPrompt() {
		return localeManager.get("card.transfer.prompt");
	}

	public void displayPlayerChangeMessage(int newPlayerIndex) {
		String format = localeManager.get("player.change.message");
		String message = String.format(format, newPlayerIndex);
		System.out.println(message);
	}

	private String getMessage(String key) {
		return localeManager.get(key);
	}
}