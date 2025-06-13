package domain;

import domain.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.*;
import ui.UserInterface;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UserInterfaceTest {
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	private final InputStream originalIn = System.in;

	private ByteArrayOutputStream outContent;
	private ByteArrayOutputStream errContent;

	@BeforeEach
	public void setUpStreams() {
		outContent = new ByteArrayOutputStream();
		errContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@AfterEach
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
		System.setIn(originalIn);
	}

	@Test
	void displayWelcome_printsExpectedHeader() {
		UserInterface ui = new UserInterface();
		assertDoesNotThrow(ui::displayWelcome);
		String out = outContent.toString();
		assertTrue(out.contains("================================="));
		assertTrue(out.contains("	EXPLODING KITTENS"));
		assertTrue(out.contains("================================="));
	}

	@Test
	void displayHelp_printsAllCommands() {
		UserInterface ui = new UserInterface();
		ui.displayHelp();
		String out = outContent.toString();
		assertTrue(out.contains("Available commands:"));
		assertTrue(out.contains("play <index>"));
		assertTrue(out.contains("draw"));
		assertTrue(out.contains("hand"));
		assertTrue(out.contains("status"));
		assertTrue(out.contains("help"));
		assertTrue(out.contains("quit"));
	}

	@Test
	void displayError_printsToStderr() {
		UserInterface ui = new UserInterface();
		ui.displayError("oops");
		String err = errContent.toString();
		assertTrue(err.contains("Error: oops"));
	}

	@Test
	void getUserInput_readsLineAndPrompts() {
		System.setIn(new ByteArrayInputStream("hello world\n".getBytes()));
		UserInterface ui = new UserInterface();
		String result = ui.getUserInput();
		assertEquals("hello world", result);
		assertTrue(outContent.toString().contains("> "));
	}

	@Test
	void getNumberOfPlayers_validFirst_tryReturnsImmediately() {
		System.setIn(new ByteArrayInputStream("3\n".getBytes()));
		UserInterface ui = new UserInterface();
		int n = ui.getNumberOfPlayers();
		final int NUMBER_OF_PLAYERS = 3;
		assertEquals(NUMBER_OF_PLAYERS, n);
		assertEquals("", errContent.toString());
	}

	@Test
	void getNumberOfPlayers_invalidThenValid_promptsUntilGood() {
		String input = String.join("\n",
				"foo", "6", "2");
		System.setIn(new ByteArrayInputStream((input + "\n").getBytes()));
		UserInterface ui = new UserInterface();
		int n = ui.getNumberOfPlayers();
		final int NUMBER_OF_PLAYERS = 2;
		assertEquals(NUMBER_OF_PLAYERS, n);
		String err = errContent.toString();
		int occurrences = err.split(
				"Please enter a number between 2 and 5", -1)
				.length - 1;
		final int NUM_OF_OCCURENCES = 2;
		assertEquals(NUM_OF_OCCURENCES, occurrences);
	}

	@Test
	void displayPlayerHand_empty_showsEmptyMessage() {
		UserInterface ui = new UserInterface();
		Player p = new Player(new Hand());
		ui.displayPlayerHand(p);
		assertTrue(outContent.toString().contains("(empty)"));
	}

	@Test
	void displayPlayerHand_withCards_listsAll() {
		UserInterface ui = new UserInterface();
		Hand h = new Hand();
		h.addCard(new SkipCard());
		h.addCard(new SkipCard());
		Player p = new Player(h);
		ui.displayPlayerHand(p);
		String out = outContent.toString();
		assertTrue(out.contains("0: SKIP"));
		assertTrue(out.contains("1: SKIP"));
	}

	@Test
	void displayCardPlayed_showCorrectText() {
		UserInterface ui = new UserInterface();
		Card c = new SkipCard();

		ui.displayCardPlayed(c);
		assertTrue(outContent.toString().contains("You played: SKIP"));
		outContent.reset();
	}

	@Test
	void displayCardPlayed_andDrawnCard_showCorrectText() {
		UserInterface ui = new UserInterface();
		Card card = new SkipCard();

		ui.displayCardPlayed(card);
		assertTrue(outContent.toString().contains("You played: SKIP"));
		outContent.reset();

		ui.displayDrawnCard(card);
		assertTrue(outContent.toString().contains("You drew: SKIP"));
	}
}
