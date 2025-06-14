package ui;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UserInterface {
	private final Scanner scanner;

	public UserInterface() {
		this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
	}

	public void displayError(String message) {
		System.err.println("Error: " + message);
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