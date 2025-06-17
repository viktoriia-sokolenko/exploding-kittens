package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class GameSetupIntegrationTest {
	private final PrintStream originalOut = System.out;
	private final InputStream originalIn = System.in;

	private ByteArrayOutputStream outContent;

	@BeforeEach
	public void setUp() {
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent,
				true, StandardCharsets.UTF_8));
	}

	@AfterEach
	public void tearDown() {
		System.setOut(originalOut);
		System.setIn(originalIn);
	}

	@Test
	public void testGameInitiatializeForLocaleTwoAndFourPlayers() {
		String simulatedInput = "2\n5\nstatus\ndraw\nquit";
		System.setIn(new ByteArrayInputStream(
				simulatedInput.getBytes(StandardCharsets.UTF_8)));

		GameEngine game = GameEngine.createNewGame();
		game.initializeGame();

		String setupOutput = outContent.toString(StandardCharsets.UTF_8);
		checkForLocaleChoiceDisplayed(setupOutput);
		checkForGameTitleDisplayed(setupOutput);
		checkForHelpDisplayed(setupOutput);

		outContent.reset();

		game.runGameLoop();

		String loopOutput = outContent.toString(StandardCharsets.UTF_8);

		checkForGameStatusBeforeAnyDrawn(loopOutput);
		checkForGameStateForFirstPlayer(loopOutput);
		checkForDrawnCard(loopOutput);
	}

	private void checkForGameTitleDisplayed(String output) {
		assertTrue(output.contains("EXPLODING KITTENS US VERSION"));
	}

	private void checkForLocaleChoiceDisplayed(String output) {
		assertTrue (output.contains("Choose language: " +
				"1 -> English, 2 -> English (US)"));
	}

	private void checkForHelpDisplayed(String output) {
		assertTrue(output.contains("Available commands:"));
		assertTrue(output.contains("play"));
		assertTrue(output.contains("draw"));
		assertTrue(output.contains("hand"));
		assertTrue(output.contains("status"));
		assertTrue(output.contains("help"));
		assertTrue(output.contains("quit"));
	}

	private void checkForGameStateForFirstPlayer(String output) {
		assertTrue (output.contains("Turn of player 0"));
		assertTrue (output.contains("Players remaining: 5"));
		assertTrue (output.contains("Active players indices: [0, 1, 2, 3, 4]"));
		assertTrue (output.contains("Cards in deck: 23"));
		assertTrue (output.contains("YOUR HAND (5 cards):"));
	}

	private void checkForGameStatusBeforeAnyDrawn (String output) {
		assertTrue (output.contains("== GAME STATUS ==="));
		assertTrue (output.contains("Active players: 5"));
		assertTrue (output.contains("Cards in deck: 23"));
		assertTrue (output.contains("Current player has 5 cards"));
	}

	private void checkForDrawnCard(String output) {
		assertTrue (output.contains("You drew: "));
	}
}
