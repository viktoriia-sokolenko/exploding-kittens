package ui;


import domain.CardType;
import domain.Player;
import domain.Card;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UserInterface {
	private final Scanner scanner;
	private static final int MAX_NUMBER_OF_PLAYERS = 5;
	private static final int MIN_NUMBER_OF_PLAYERS = 2;

	public UserInterface() {
		this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
	}

	public void displayWelcome() {
		final String border = "=================================";
		System.out.println(border);
		System.out.println("\tEXPLODING KITTENS");
		System.out.println(border + "\n");
	}

	public void displayHelp() {
		System.out.println("\nAvailable commands:");
		System.out.println("  play <index>	- Play a card from your hand " +
				"(0-based index)");
		System.out.println("  draw			- Draw a card and end your turn");
		System.out.println("  hand			- Show your current hand");
		System.out.println("  status		- Show game status");
		System.out.println("  help			- Show this help message");
		System.out.println("  quit			- Exit the game\n");
	}

	public void displayError(String message) {
		System.err.println("Error: " + message);
	}

	public void displaySuccess(String message) {
		System.out.println("Success: " + message);
	}

	public void displayWarning(String message) {
		System.err.println("Warning: " + message);
	}

	public String getUserInput() {
		System.out.print("> ");
		return scanner.nextLine();
	}

	public int getNumberOfPlayers() {
		while (true) {
			System.out.print("How many players? (2-5)");
			String input = scanner.nextLine();
			try {
				int numberOfPlayers = Integer.parseInt(input);
				if (numberOfPlayers >= MIN_NUMBER_OF_PLAYERS &&
						numberOfPlayers <= MAX_NUMBER_OF_PLAYERS) {
					return numberOfPlayers;
				} else {
					displayError("Please enter a number between 2 and 5");
				}
			} catch (NumberFormatException ignored) {
				displayError("Please enter a number between 2 and 5");
			}
		}
	}


	public void displayPlayerHand(Player player) {
		int total = player.getNumberOfCards();

		final int NUMBER_OF_DASHES = 40;
		final int NO_CARDS = 0;
		System.out.println("\n" + "─".repeat(NUMBER_OF_DASHES));
		System.out.println("YOUR HAND (" + total + " cards):");
		System.out.println("─".repeat(NUMBER_OF_DASHES));

		if (total == NO_CARDS) {
			System.out.println("  (empty hand)");
		} else {
			for (CardType type : CardType.values()) {
				Integer countInteger = player.getCardTypeCount(type);
				int count = (countInteger != null) ? countInteger : 0;

				if (count > 0) {
					String cardDisplay = formatCardName(type);
					String typeName = type.name().toLowerCase()
							.replace("_", " ");
					if (count == 1) {
						System.out.printf("	 %s (type: %s)%n",
								cardDisplay, typeName);
					} else {
						System.out.printf("	 %s x%d (type: %s)%n",
								cardDisplay, count, typeName);
					}
				}
			}
		}

		System.out.println("─".repeat(NUMBER_OF_DASHES));
		System.out.println("Use 'play <type>' to play a card (e.g., 'play skip')");
		System.out.println("─".repeat(NUMBER_OF_DASHES) + "\n");
	}


	public void displayCardPlayed(Card card) {
		System.out.println("You played: " + card.getCardType() + "\n");
		displayCardEffect(card.getCardType());
		System.out.println();
	}

	public void displayDrawnCard(Card card) {
		if (card.getCardType() == CardType.EXPLODING_KITTEN) {
			System.out.println("OH NO! You drew: " +
					formatCardName(card.getCardType()));
		}

		System.out.println("You drew: " + formatCardName(card.getCardType()));
		System.out.println();
	}

	public void displayCardEffect(CardType cardType) {
		switch (cardType) {
			case ATTACK:
				System.out.println("   " +
						"→ End your turn without drawing," +
						" next player takes 2 turns");
				break;
			case SKIP:
				System.out.println("   → End your turn without drawing a card");
				break;
			case SEE_THE_FUTURE:
				System.out.println("   → Peek at the top cards of the deck");
				break;
			case SHUFFLE:
				System.out.println("   → Shuffle the deck");
				break;
			case FAVOR:
				System.out.println("   " +
						"→ Force another player to give you a card");
				break;
			case ALTER_THE_FUTURE:
				System.out.println("   " +
						"→ Rearrange the top cards of the deck");
				break;
			case DEFUSE:
				System.out.println("   → " +
						"Used automatically when you draw an " +
						"Exploding Kitten");
				break;
			case NUKE:
				System.out.println("   " +
						"→ Nuclear option - ends the game!");
				break;
			case NORMAL:
				System.out.println("   " +
						"→ Just a cute cat - no special effect");
				break;
			default:
				break;
		}
	}

	public String formatCardName(CardType cardType) {
		switch (cardType) {
			case EXPLODING_KITTEN:
				return "Exploding Kitten";
			case DEFUSE:
				return "Defuse";
			case ATTACK:
				return "Attack";
			case SKIP:
				return "Skip";
			case FAVOR:
				return "Favor";
			case SHUFFLE:
				return "Shuffle";
			case SEE_THE_FUTURE:
				return "See the Future";
			case ALTER_THE_FUTURE:
				return "Alter the Future";
			case NUKE:
				return "Nuke";
			case NORMAL:
				return "Normal Cat";
			default:
				return cardType.toString();
		}
	}

	public void displayTurnStart(int playerNumber, int totalPlayers) {
		System.out.println("\n" + "Player " + playerNumber + "'s" +
				"turn (Player " +
				playerNumber + " of " + totalPlayers + ")");
	}

	public void displayDeckEmpty() {
		displayWarning("The deck is empty! No more cards to draw.");
	}

	public void displayDefuseUsed() {
		System.out.println("You used a Defuse card!");
		System.out.println("Now pick where to put the Exploding Kitten card" +
				"back into the deck" +
				".\n");
	}


	public void displayPlayerEliminated() {
		System.out.println("You have been eliminated from the game!");
		System.out.println("Better luck next time!\n");
	}

	public void displayGameEnd(boolean isThereGameWinner) {
		final int NUMBER_OF_EQUAL_SIGNS = 50;
		System.out.println("\n" + "=".repeat(NUMBER_OF_EQUAL_SIGNS));
		if (isThereGameWinner) {
			System.out.println("CONGRATULATIONS! YOU WON!");
			System.out.println("You survived the exploding kittens!");
		} else {
			// this realistically though, wouldn't happen
			System.out.println("GAME OVER!");
			System.out.println("Everyone exploded!");
		}

		System.out.println("=".repeat(NUMBER_OF_EQUAL_SIGNS));
		System.out.println("Thanks for playing Exploding Kittens!");
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

	public int getNumericUserInput(String message) {
		while (true) {
			System.out.println(message);
			System.out.print("> ");
			String input = scanner.nextLine();
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException ignored) {
				displayError("Please enter a number between 2 and 5");
			}
		}
	}
}