package ui;

import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;

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
}
