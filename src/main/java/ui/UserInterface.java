package ui;

import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final int MAX_NUMBER_OF_PLAYERS = 5;
    private final int MIN_NUMBER_OF_PLAYERS = 2;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void displayWelcome() {
        System.out.println("=================================");
        System.out.println("   EXPLODING KITTENS");
        System.out.println("==================================\n");
    }

    public void displayHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  play <index>  - Play a card from your hand " +
                "(0-based index)");
        System.out.println("  draw          - Draw a card and end your turn");
        System.out.println("  hand          - Show your current hand");
        System.out.println("  status        - Show game status");
        System.out.println("  help          - Show this help message");
        System.out.println("  quit          - Exit the game\n");
    }

    public void displayError(String message) {
        System.err.println("Error: " + message);
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
}
