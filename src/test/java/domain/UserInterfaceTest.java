package domain;

import domain.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.*;
import ui.UserInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
		System.setOut(new PrintStream(outContent, true,
				StandardCharsets.UTF_8));
		System.setErr(new PrintStream(errContent, true,
				StandardCharsets.UTF_8));
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
		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("================================="));
		assertTrue(out.contains("	EXPLODING KITTENS"));
		assertTrue(out.contains("================================="));
	}

	@Test
	void displayHelp_printsAllCommands() {
		UserInterface ui = new UserInterface();
		ui.displayHelp();
		String out = outContent.toString(StandardCharsets.UTF_8);
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
		String err = errContent.toString(StandardCharsets.UTF_8);
		assertTrue(err.contains("Error: oops"));
	}

	@Test
	void getUserInput_readsLineAndPrompts() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();
		String result = ui.getUserInput();
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	void getNumberOfPlayers_validFirst_tryReturnsImmediately() {
		System.setIn(new ByteArrayInputStream("3\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();
		int numberOfPlayers = ui.getNumberOfPlayers();
		final int NUMBER_OF_PLAYERS = 3;
		assertEquals(NUMBER_OF_PLAYERS, numberOfPlayers);
		assertEquals("", errContent.toString(StandardCharsets.UTF_8));
	}

	@Test
	void getNumberOfPlayers_invalidThenValid_promptsUntilGood() {
		String input = String.join("\n",
				"foo", "6", "2");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();
		int numberOfPlayers = ui.getNumberOfPlayers();
		final int NUMBER_OF_PLAYERS = 2;
		assertEquals(NUMBER_OF_PLAYERS, numberOfPlayers);
		String err = errContent.toString(StandardCharsets.UTF_8);
		int occurrences = err.split(
				"Please enter a number between 2 and 5", -1)
				.length - 1;
		final int NUM_OF_OCCURRENCES = 2;
		assertEquals(NUM_OF_OCCURRENCES, occurrences);
	}

	@Test
	void displayPlayerHand_emptyHand_showsEmptyMessage() {
		UserInterface ui = new UserInterface();
		Hand hand = new Hand();
		Player player = new Player(hand);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (0 cards):"));
		assertTrue(out.contains("(empty hand)"));
		assertTrue(out.contains("Use 'play <type>' to play a card"));
	}

	@Test
	void displayCardPlayed_showCorrectText() {
		UserInterface ui = new UserInterface();
		Card card = new SkipCard();

		ui.displayCardPlayed(card);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).
				contains("You played: SKIP"));
		outContent.reset();
	}

	@Test
	void displayPlayerHand_singleCard_showsCardWithoutCount() {
		UserInterface ui = new UserInterface();
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		Player player = new Player(hand);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (1 cards):"));
		assertTrue(out.contains("Skip (type: skip)"));
		assertFalse(out.contains("x2"));
	}

	@Test
	void displayPlayerHand_multipleCardsOfSameType_showsCount() {
		UserInterface ui = new UserInterface();
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		Player player = new Player(hand);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (3 cards):"));
		assertTrue(out.contains("Skip x3 (type: skip)"));
	}

	@Test
	void displayPlayerHand_multipleDifferentCards_showsAll() {
		UserInterface ui = new UserInterface();
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		hand.addCard(new AttackCard());
		Player player = new Player(hand);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (3 cards):"));
		assertTrue(out.contains("Skip x2 (type: skip)"));
		assertTrue(out.contains("Attack (type: attack)"));
	}

	@Test
	void displayCardPlayed_andDrawnCard_showCorrectText() {
		UserInterface ui = new UserInterface();
		Card card = new SkipCard();

		ui.displayCardPlayed(card);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).
				contains("You played: SKIP"));
		outContent.reset();

		ui.displayDrawnCard(card);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).
				contains("You drew: Skip"));
	}

	@Test
	void displayDrawnCard_explodingKitten_printsSpecialMessage() {
		UserInterface ui = new UserInterface();
		Card card = new ExpoldingKittenCard();

		ui.displayDrawnCard(card);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("OH NO! You drew: Exploding Kitten"));
	}


	@Test
	void displayCardPlayed_printsCardWithEffect() {
		UserInterface ui = new UserInterface();
		Card card = new SkipCard();

		ui.displayCardPlayed(card);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("You played: SKIP"));
		assertTrue(out.contains(" → End your turn without drawing a card"));
	}

	@Test
	void displayCardEffect_allCardTypes_printsCorrectEffects() {
		UserInterface ui = new UserInterface();

		ui.displayCardEffect(CardType.ATTACK);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → End your turn without drawing, " +
						"next player takes 2 turns"));
		outContent.reset();

		ui.displayCardEffect(CardType.SKIP);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("" +
				" → End your turn without drawing a card"));
		outContent.reset();

		ui.displayCardEffect(CardType.SEE_THE_FUTURE);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains(" " +
				"→ Peek at the top cards of the deck"));
		outContent.reset();

		ui.displayCardEffect(CardType.SHUFFLE);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → Shuffle the deck"));
		outContent.reset();

		ui.displayCardEffect(CardType.FAVOR);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → Force another player to give you a card"));
		outContent.reset();

		ui.displayCardEffect(CardType.ALTER_THE_FUTURE);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → Rearrange the top cards of the deck"));
		outContent.reset();

		ui.displayCardEffect(CardType.DEFUSE);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → Used automatically when you draw an " +
						"Exploding Kitten"));
		outContent.reset();

		ui.displayCardEffect(CardType.NUKE);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → Nuclear option - ends the game!"));
		outContent.reset();

		ui.displayCardEffect(CardType.NORMAL);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → Just a cute cat - no special effect"));
		outContent.reset();

		ui.displayCardEffect(CardType.EXPLODING_KITTEN);
		assertEquals("", outContent.toString(StandardCharsets.UTF_8));
	}

	@Test
	void formatCardName_allCardTypes_returnsCorrectFormat() {
		UserInterface ui = new UserInterface();
		assertEquals("Exploding Kitten",
				ui.formatCardName(CardType.EXPLODING_KITTEN));
		assertEquals("Defuse", ui.formatCardName(CardType.DEFUSE));
		assertEquals("Attack", ui.formatCardName(CardType.ATTACK));
		assertEquals("Skip", ui.formatCardName(CardType.SKIP));
		assertEquals("Favor", ui.formatCardName(CardType.FAVOR));
		assertEquals("Shuffle", ui.
				formatCardName(CardType.SHUFFLE));
		assertEquals("See the Future",
				ui.formatCardName(CardType.SEE_THE_FUTURE));
		assertEquals("Alter the Future",
				ui.formatCardName(CardType.ALTER_THE_FUTURE));
		assertEquals("Nuke", ui.formatCardName(CardType.NUKE));
		assertEquals("Normal Cat",
				ui.formatCardName(CardType.NORMAL));
	}

	@Test
	void displaySuccess_printsSuccessMessage() {
		UserInterface ui = new UserInterface();
		String testMessage = "You played: SKIP";

		ui.displaySuccess(testMessage);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Success: " + testMessage));
	}

	@Test
	void displayWarning_printsWarningMessage() {
		UserInterface ui = new UserInterface();
		String testMessage = "There are only a few cards left in the deck";

		ui.displayWarning(testMessage);

		String output = errContent.toString(StandardCharsets.UTF_8);
		String expectedOutput = "Warning: " + testMessage;

		assertTrue(output.contains(expectedOutput));
	}

	@Test
	void displayTurnStart_printsCorrectTurnInfo() {
		UserInterface ui = new UserInterface();
		final int CURRENT_PLAYER = 2;
		final int TOTAL_PLAYERS = 4;

		ui.displayTurnStart(CURRENT_PLAYER, TOTAL_PLAYERS);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Player 2'sturn (Player 2 of 4)"));
		assertTrue(out.startsWith("\n"));
	}

	@Test
	void displayDeckEmpty_printsWarningMessage() {
		UserInterface ui = new UserInterface();

		ui.displayDeckEmpty();

		String err = errContent.toString(StandardCharsets.UTF_8);
		assertTrue(err.contains("Warning: The deck is empty! " +
				"No more cards to draw."));
	}

	@Test
	void displayDefuseUsed_printsDefuseMessage() {
		UserInterface ui = new UserInterface();

		ui.displayDefuseUsed();

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("You used a Defuse card!"));
		assertTrue(out.contains("Now pick where " +
				"to put the Exploding Kitten cardback into the deck"));
	}

	@Test
	void displayPlayerEliminated_printsPlayerEliminatedMessage() {
		UserInterface ui = new UserInterface();

		ui.displayPlayerEliminated();

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("You have been eliminated from the game!"));
		assertTrue(out.contains("Better luck next time!"));
	}


	@Test
	void displayGameEnd_withWinner_printsVictoryMessage() {
		UserInterface ui = new UserInterface();

		ui.displayGameEnd(true);
		final int NUMBER_OF_EQUAL_SIGNS = 50;
		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("CONGRATULATIONS! YOU WON!"));
		assertTrue(out.contains("You survived the exploding kittens!"));
		assertTrue(out.contains("Thanks for playing Exploding Kittens!"));
		assertTrue(out.contains("=".repeat(NUMBER_OF_EQUAL_SIGNS)));
	}

	@Test
	void displayGameEnd_noWinner_printsGameOverMessage() {
		UserInterface ui = new UserInterface();
		final int NUMBER_OF_EQUAL_SIGNS = 50;
		ui.displayGameEnd(false);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("GAME OVER!"));
		assertTrue(out.contains("Everyone exploded!"));
		assertTrue(out.contains("Thanks for playing Exploding Kittens!"));
		assertTrue(out.contains("=".repeat(NUMBER_OF_EQUAL_SIGNS)));
	}

	@Test
	void getNumberOfPlayers_minimumBoundary_acceptsMinimumValue() {
		System.setIn(new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();
		int numberOfPlayers = ui.getNumberOfPlayers();
		final int EXPECTED_NUMBER_OF_PLAYERS = 2;
		assertEquals(EXPECTED_NUMBER_OF_PLAYERS, numberOfPlayers);
		assertEquals("", errContent.toString(StandardCharsets.UTF_8));
	}

	@Test
	void getNumberOfPlayers_belowMinimum_rejectsAndPrompts() {
		String input = String.join("\n", "1", "2");
		System.setIn(new ByteArrayInputStream((input + "\n").getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface();
		int numberOfPlayers = ui.getNumberOfPlayers();
		final int EXPECTED_NUMBER_OF_PLAYERS = 2;
		assertEquals(EXPECTED_NUMBER_OF_PLAYERS, numberOfPlayers);
		String err = errContent.toString(StandardCharsets.UTF_8);
		assertTrue(err.contains("Please enter a number between 2 and 5"));
	}
}