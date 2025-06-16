package domain;

import locale.LocaleManager;
import org.easymock.EasyMock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ui.UserInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserInterfaceTest {
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	private final InputStream originalIn = System.in;
	private static final int MAX_PLAYERS = 5;
	private LocaleManager localeManager;

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
		
		localeManager = EasyMock.createMock(LocaleManager.class);
	}


	@AfterEach
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
		System.setIn(originalIn);
	}

	@Test
	public void displayWelcome_printsExpectedHeader() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("exploding.kittens"))
				.andReturn("	EXPLODING KITTENS");
		EasyMock.replay(localeManager);

		assertDoesNotThrow(ui::displayWelcome);
		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("================================="));
		assertTrue(out.contains("	EXPLODING KITTENS"));
		assertTrue(out.contains("================================="));
	}

	@Test
	public void displayHelp_printsAllCommands() {
		UserInterface ui = new UserInterface(localeManager);
		EasyMock.expect(localeManager.get("commands.available"))
				.andReturn("Available commands:");
		EasyMock.expect(localeManager.get("command.play"))
				.andReturn("  play <index>\t- " +
						"Play a card from your hand (0-based index)");
		EasyMock.expect(localeManager.get("command.draw"))
				.andReturn("  draw\t\t- Draw a card and end your turn");
		EasyMock.expect(localeManager.get("command.hand"))
				.andReturn("  hand\t\t- Show your current hand");
		EasyMock.expect(localeManager.get("command.status"))
				.andReturn("  status\t\t- Show game status");
		EasyMock.expect(localeManager.get("command.help"))
				.andReturn("  help\t\t- Show this help message");
		EasyMock.expect(localeManager.get("command.quit"))
				.andReturn("  quit\t\t- Exit the game");

		EasyMock.replay(localeManager);

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
	public void displayError_printsToStderr() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ");
		EasyMock.replay(localeManager);

		ui.displayError("oops");
		String err = errContent.toString(StandardCharsets.UTF_8);
		assertTrue(err.contains("Error: oops"));
	}

	@Test
	public void getUserInput_readsLineAndPrompts() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);
		String result = ui.getUserInput();
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getNumberOfPlayers_validFirst_tryReturnsImmediately() {
		System.setIn(new ByteArrayInputStream("3\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);
		int numberOfPlayers = ui.getNumberOfPlayers();
		final int NUMBER_OF_PLAYERS = 3;
		assertEquals(NUMBER_OF_PLAYERS, numberOfPlayers);
		assertEquals("", errContent.toString(StandardCharsets.UTF_8));
	}

	@Test
	public void getNumberOfPlayers_invalidThenValid_promptsUntilGood() {
		String input = String.join("\n",
				"foo", "6", "2");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("how.many.players"))
				.andReturn("How many players? (2-5)").anyTimes();
		EasyMock.expect(localeManager.get("error.players.number"))
				.andReturn("Please enter a number between 2 and 5").anyTimes();
		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ").anyTimes();
		EasyMock.replay(localeManager);

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
	public void displayPlayerHand_emptyHand_showsEmptyMessage() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.empty"))
				.andReturn("(empty hand)");
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (0 cards):"));
		assertTrue(out.contains("(empty hand)"));
		assertTrue(out.contains("Use 'play <type>' to play a card"));
	}

	@Test
	public void displayCardPlayed_showCorrectText() {
		UserInterface ui = new UserInterface(localeManager);
		Card card = new SkipCard();

		EasyMock.expect(localeManager.get("card.played"))
				.andReturn("You played: ");
		EasyMock.expect(localeManager.get("card.effect.skip"))
				.andReturn("→ End your turn without drawing a card");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayCardPlayed(card);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).
				contains("You played: Skip"));
		outContent.reset();
	}

	@Test
	public void displayPlayerHand_singleCard_showsCardWithoutCount() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type").anyTimes();
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (1 cards):"));
		assertTrue(out.contains("Skip (type: skip)"));
		assertFalse(out.contains("x2"));
	}

	@Test
	public void displayPlayerHand_multipleCardsOfSameType_showsCount() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type").anyTimes();
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (3 cards):"));
		assertTrue(out.contains("Skip x3 (type: skip)"));
	}

	@Test
	public void displayPlayerHand_multipleDifferentCards_showsAll() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		hand.addCard(new AttackCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type").anyTimes();
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (3 cards):"));
		assertTrue(out.contains("Skip x2 (type: skip)"));
		assertTrue(out.contains("Attack (type: attack)"));
	}

	@Test
	public void displayCardPlayed_andDrawnCard_showCorrectText() {
		UserInterface ui = new UserInterface(localeManager);
		Card card = new SkipCard();

		EasyMock.expect(localeManager.get("card.played"))
				.andReturn("You played: ");
		EasyMock.expect(localeManager.get("card.drawn"))
				.andReturn("You drew: ");
		EasyMock.expect(localeManager.get("card.effect.skip"))
				.andReturn("→ End your turn without drawing a card");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayCardPlayed(card);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).
				contains("You played: Skip"));
		outContent.reset();

		ui.displayDrawnCard(card);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).
				contains("You drew: Skip"));
	}

	@Test
	public void displayDrawnCard_explodingKitten_printsSpecialMessage() {
		UserInterface ui = new UserInterface(localeManager);
		Card card = new ExpoldingKittenCard();

		EasyMock.expect(localeManager.get("card.drawn.exploding"))
				.andReturn("OH NO! You drew: ");
		EasyMock.expect(localeManager.get("card.drawn"))
				.andReturn("You drew: ");
		EasyMock.expect(localeManager.get("card.name.exploding_kitten"))
				.andReturn("Exploding Kitten").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayDrawnCard(card);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("OH NO! You drew: Exploding Kitten"));
	}


	@Test
	public void displayCardPlayed_printsCardWithEffect() {
		UserInterface ui = new UserInterface(localeManager);
		Card card = new SkipCard();

		EasyMock.expect(localeManager.get("card.played"))
				.andReturn("You played: ");
		EasyMock.expect(localeManager.get("card.effect.skip"))
				.andReturn("→ End your turn without drawing a card");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayCardPlayed(card);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("You played: Skip"));
		assertTrue(out.contains(" → End your turn without drawing a card"));
	}

	@Test
	public void displayCardEffect_allCardTypes_printsCorrectEffects() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("card.effect.attack"))
				.andReturn("→ End your turn without drawing, " +
						"next player takes 2 turns");
		EasyMock.expect(localeManager.get("card.effect.skip"))
				.andReturn("→ End your turn without drawing " +
						"a card");
		EasyMock.expect(localeManager.get("card.effect.see_the_future"))
				.andReturn("→ Peek at the top cards of the deck");
		EasyMock.expect(localeManager.get("card.effect.shuffle"))
				.andReturn("→ Shuffle the deck");
		EasyMock.expect(localeManager.get("card.effect.favor"))
				.andReturn("→ Force another player to give you a card");
		EasyMock.expect(localeManager.get("card.effect.alter_the_future"))
				.andReturn("→ Rearrange the top cards of the deck");
		EasyMock.expect(localeManager.get("card.effect.defuse"))
				.andReturn("→ Used automatically when you draw " +
						"an Exploding Kitten");
		EasyMock.expect(localeManager.get("card.effect.nuke"))
				.andReturn("→ Nuclear option - " +
						"ends the game!");
		EasyMock.expect(localeManager.get("card.effect.reverse"))
				.andReturn("→ Reverse the order of play and end " +
						"your turn without drawing a card");
		EasyMock.expect(localeManager.get("card.effect.swap_top_and_bottom"))
				.andReturn("→ Swap the top and bottom cards of the deck");
		EasyMock.expect(localeManager.get("card.effect.exploding_kitten"))
				.andReturn("");
		EasyMock.expect(localeManager.get("card.effect.normal"))
				.andReturn("→ Just a cute cat - no special effect");
		EasyMock.replay(localeManager);

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
		outContent.reset();

		ui.displayCardEffect(CardType.REVERSE);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains(" → Reverse the order of play " +
						"and end your turn without drawing a card"));

		ui.displayCardEffect(CardType.SWAP_TOP_AND_BOTTOM);
		assertTrue(outContent.toString(StandardCharsets.UTF_8)
				.contains (" → Swap the top and bottom cards of the deck"));

	}

	@Test
	public void formatCardName_allCardTypes_returnsCorrectFormat() {
		UserInterface ui = new UserInterface(localeManager);
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.defuse"))
				.andReturn("Defuse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.see_the_future"))
				.andReturn("See the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.exploding_kitten"))
				.andReturn("Exploding Kitten").anyTimes();
		EasyMock.expect(localeManager.get("card.name.normal"))
				.andReturn("Normal Cat").anyTimes();
		EasyMock.expect(localeManager.get("card.name.favor"))
				.andReturn("Favor").anyTimes();
		EasyMock.expect(localeManager.get("card.name.shuffle"))
				.andReturn("Shuffle").anyTimes();
		EasyMock.expect(localeManager.get("card.name.unknown_card_for_test"))
				.andReturn("").anyTimes();
		EasyMock.expect(localeManager.get("card.name.alter_the_future"))
				.andReturn("Alter the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.nuke"))
				.andReturn("Nuke").anyTimes();
		EasyMock.expect(localeManager.get("card.name.reverse"))
				.andReturn("Reverse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.swap_top_and_bottom"))
				.andReturn("Swap Top and Bottom").anyTimes();
		EasyMock.replay(localeManager);

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
	public void displaySuccess_printsSuccessMessage() {
		UserInterface ui = new UserInterface(localeManager);
		String testMessage = "You played: SKIP";

		EasyMock.expect(localeManager.get("success"))
				.andReturn("Success: ");
		EasyMock.replay(localeManager);

		ui.displaySuccess(testMessage);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Success: " + testMessage));
	}

	@Test
	public void displayWarning_printsWarningMessage() {
		UserInterface ui = new UserInterface(localeManager);
		String testMessage = "There are only a few cards left in the deck";

		EasyMock.expect(localeManager.get("warning"))
				.andReturn("Warning: ").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayWarning(testMessage);

		String output = errContent.toString(StandardCharsets.UTF_8);
		String expectedOutput = "Warning: " + testMessage;

		assertTrue(output.contains(expectedOutput));
	}

	@Test
	public void displayTurnStart_printsCorrectTurnInfo() {
		UserInterface ui = new UserInterface(localeManager);
		EasyMock.expect(localeManager.get("turn.start"))
				.andReturn("Player %d's turn (Player %d of %d)");
		EasyMock.expect(localeManager.get("warning"))
				.andReturn("Warning: ").anyTimes();
		EasyMock.replay(localeManager);

		final int CURRENT_PLAYER = 2;
		final int TOTAL_PLAYERS = 4;

		ui.displayTurnStart(CURRENT_PLAYER, TOTAL_PLAYERS);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Player 2's turn (Player 2 of 4)"));
		assertTrue(out.startsWith("\n"));
	}

	@Test
	public void displayDeckEmpty_printsWarningMessage() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("deck.empty"))
				.andReturn("The deck is empty! No more cards to draw.");
		EasyMock.expect(localeManager.get("warning"))
				.andReturn("Warning: ").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayDeckEmpty();

		String err = errContent.toString(StandardCharsets.UTF_8);
		assertTrue(err.contains("Warning: The deck is empty! " +
				"No more cards to draw."));
	}

	@Test
	public void displayDefuseUsed_printsDefuseMessage() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("card.defuse.used"))
				.andReturn("You used a Defuse card!");
		EasyMock.expect(localeManager.get("card.defuse.place.kitten"))
				.andReturn("Now pick where to put the Exploding " +
						"Kitten card back into the deck.\n");
		EasyMock.replay(localeManager);

		ui.displayDefuseUsed();

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("You used a Defuse card!"));
		assertTrue(out.contains("Now pick where " +
				"to put the Exploding Kitten card back into the deck"));
	}

	@Test
	public void displayPlayerEliminated_printsPlayerEliminatedMessage() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("player.eliminated"))
				.andReturn("You have been eliminated from the game!");
		EasyMock.expect(localeManager.get("better.luck.wishes"))
				.andReturn("Better luck next time!\n");
		EasyMock.replay(localeManager);

		ui.displayPlayerEliminated();

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("You have been eliminated from the game!"));
		assertTrue(out.contains("Better luck next time!"));
	}


	@Test
	public void displayGameEnd_withWinner_printsVictoryMessage() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("game.win"))
				.andReturn("CONGRATULATIONS! YOU WON!");
		EasyMock.expect(localeManager.get("game.win.survive.exploding"))
				.andReturn("You survived the exploding kittens!");
		EasyMock.expect(localeManager.get("game.quit.thanks"))
				.andReturn("Thanks for playing Exploding Kittens!");
		EasyMock.replay(localeManager);

		ui.displayGameEnd(true);
		final int NUMBER_OF_EQUAL_SIGNS = 50;
		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("CONGRATULATIONS! YOU WON!"));
		assertTrue(out.contains("You survived the exploding kittens!"));
		assertTrue(out.contains("Thanks for playing Exploding Kittens!"));
		assertTrue(out.contains("=".repeat(NUMBER_OF_EQUAL_SIGNS)));
	}

	@Test
	public void displayGameEnd_noWinner_printsGameOverMessage() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("game.lose"))
				.andReturn("GAME OVER!");
		EasyMock.expect(localeManager.get("game.lose.exploded"))
				.andReturn("Everyone exploded!");
		EasyMock.expect(localeManager.get("game.quit.thanks"))
				.andReturn("Thanks for playing Exploding Kittens!");
		EasyMock.replay(localeManager);

		final int NUMBER_OF_EQUAL_SIGNS = 50;
		ui.displayGameEnd(false);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("GAME OVER!"));
		assertTrue(out.contains("Everyone exploded!"));
		assertTrue(out.contains("Thanks for playing Exploding Kittens!"));
		assertTrue(out.contains("=".repeat(NUMBER_OF_EQUAL_SIGNS)));
	}

	@Test
	public void getNumberOfPlayers_minimumBoundary_acceptsMinimumValue() {
		System.setIn(new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);
		int numberOfPlayers = ui.getNumberOfPlayers();
		final int EXPECTED_NUMBER_OF_PLAYERS = 2;
		assertEquals(EXPECTED_NUMBER_OF_PLAYERS, numberOfPlayers);
		assertEquals("", errContent.toString(StandardCharsets.UTF_8));
	}

	@Test
	public void getNumberOfPlayers_belowMinimum_rejectsAndPrompts() {
		String input = String.join("\n", "1", "2");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("how.many.players"))
				.andReturn("How many players? (2-5)").anyTimes();
		EasyMock.expect(localeManager.get("error.players.number"))
				.andReturn("Please enter a number between 2 and 5").anyTimes();
		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ").anyTimes();
		EasyMock.replay(localeManager);

		int numberOfPlayers = ui.getNumberOfPlayers();
		final int EXPECTED_NUMBER_OF_PLAYERS = 2;
		assertEquals(EXPECTED_NUMBER_OF_PLAYERS, numberOfPlayers);
		String err = errContent.toString(StandardCharsets.UTF_8);
		assertTrue(err.contains("Please enter a number between 2 and 5"));
	}

	@Test
	public void getNumberOfPlayers_maximumBoundary_acceptsMaximumValue() {
		System.setIn(new ByteArrayInputStream("5\n".getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);
		int numberOfPlayers = ui.getNumberOfPlayers();
		final int EXPECTED_NUMBER_OF_PLAYERS = 5;
		assertEquals(EXPECTED_NUMBER_OF_PLAYERS, numberOfPlayers);
		assertEquals("", errContent.toString(StandardCharsets.UTF_8));
	}

	@Test
	public void getNumberOfPlayers_aboveMaximum_rejectsAndPrompts() {
		String input = String.join("\n", "6", "3");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("how.many.players"))
				.andReturn("How many players? (2-5)").anyTimes();
		EasyMock.expect(localeManager.get("error.players.number"))
				.andReturn("Please enter a number between 2 and 5").anyTimes();
		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ").anyTimes();
		EasyMock.replay(localeManager);

		int numberOfPlayers = ui.getNumberOfPlayers();
		final int EXPECTED_NUMBER_OF_PLAYERS = 3;
		assertEquals(EXPECTED_NUMBER_OF_PLAYERS, numberOfPlayers);
		String err = errContent.toString(StandardCharsets.UTF_8);
		assertTrue(err.contains("Please enter a number between 2 and 5"));
	}

	@Test
	public void displayPlayerHand_singleCard_doesNotDisplayOtherCardTypes() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type");
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.defuse"))
				.andReturn("Defuse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.expect(localeManager.get("card.name.favor"))
				.andReturn("Favor").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);

		assertTrue(out.contains("Skip (type: skip)"));
		assertFalse(out.contains("Attack"));
		assertFalse(out.contains("Defuse"));
		assertFalse(out.contains("Favor"));
		assertFalse(out.contains("x0"));
	}

	@Test
	public void displayPlayerHand_multipleCards_onlyShowsPlayerCards() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		hand.addCard(new AttackCard());
		hand.addCard(new AttackCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type").anyTimes();
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Skip (type: skip)"));
		assertTrue(out.contains("Attack x2 (type: attack)"));
		assertFalse(out.contains("Defuse"));
		assertFalse(out.contains("Favor"));
		assertFalse(out.contains("Shuffle"));
		assertFalse(out.contains("x0"));
	}

	@Test
	public void displayPlayerHand_verifiesExactCountDisplay() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		hand.addCard(new SkipCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type");
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Skip x4 (type: skip)"));
		assertFalse(out.contains("Skip x0"));
		assertFalse(out.contains("Skip x1"));
		assertFalse(out.contains("Skip x2"));
		assertFalse(out.contains("Skip x3"));
	}

	@Test
	public void displayPlayerHand_mixedCards_showsCorrectCounts() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();

		hand.addCard(new SkipCard());
		hand.addCard(new AttackCard());
		hand.addCard(new AttackCard());
		hand.addCard(new AttackCard());
		hand.addCard(new DefuseCard());
		hand.addCard(new DefuseCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type").anyTimes();
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.defuse"))
				.andReturn("Defuse").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Skip (type: skip)"));
		assertTrue(out.contains("Attack x3 (type: attack)"));
		assertTrue(out.contains("Defuse x2 (type: defuse)"));
		assertFalse(out.contains("Favor"));
		assertFalse(out.contains("Exploding Kitten"));
		assertFalse(out.contains("x0"));
	}

	@Test
	public void formatCardName_verifyExactStringMatching() {
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		UserInterface ui = new UserInterface(localeManager);
		assertEquals("Skip", ui.formatCardName(CardType.SKIP));
		assertNotEquals("SKIP", ui.formatCardName(CardType.SKIP));
		assertNotEquals("skip", ui.formatCardName(CardType.SKIP));
		assertNotEquals("", ui.formatCardName(CardType.SKIP));

		assertEquals("Attack", ui.formatCardName(CardType.ATTACK));
		assertNotEquals("ATTACK", ui.
				formatCardName(CardType.ATTACK));
	}

	@Test
	public void formatCardName_defaultCase_returnsToString() {
		UserInterface ui = new UserInterface(localeManager);
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.defuse"))
				.andReturn("Defuse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.see_the_future"))
				.andReturn("See the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.exploding_kitten"))
				.andReturn("Exploding Kitten").anyTimes();
		EasyMock.expect(localeManager.get("card.name.normal"))
				.andReturn("Normal Cat").anyTimes();
		EasyMock.expect(localeManager.get("card.name.favor"))
				.andReturn("Favor").anyTimes();
		EasyMock.expect(localeManager.get("card.name.shuffle"))
				.andReturn("Shuffle").anyTimes();
		EasyMock.expect(localeManager.get("card.name.unknown_card_for_test"))
				.andReturn("").anyTimes();
		EasyMock.expect(localeManager.get("card.name.alter_the_future"))
				.andReturn("Alter the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.nuke"))
				.andReturn("Nuke").anyTimes();
		EasyMock.expect(localeManager.get("card.name.reverse"))
				.andReturn("Reverse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.swap_top_and_bottom"))
				.andReturn("Swap Top and Bottom").anyTimes();
		EasyMock.replay(localeManager);

		for (CardType type : CardType.values()) {
			String result = ui.formatCardName(type);
			assertNotNull(result);
			assertFalse(result.isEmpty());
			if (type == CardType.SEE_THE_FUTURE) {
				assertNotEquals(type.toString(), result);
				assertEquals("See the Future", result);
			}
		}
	}

	@Test
	public void displayPlayerHand_verifyNullCountHandling() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.empty"))
				.andReturn("(empty hand)");
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.normal"))
				.andReturn("Normal").anyTimes();
		EasyMock.expect(localeManager.get("card.name.exploding_kitten"))
				.andReturn("Exploding Kitten").anyTimes();
		EasyMock.expect(localeManager.get("card.name.see_the_future"))
				.andReturn("See the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.defuse"))
				.andReturn("Defuse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.expect(localeManager.get("card.name.alter_the_future"))
				.andReturn("Alter the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.shuffle"))
				.andReturn("Shuffle").anyTimes();
		EasyMock.expect(localeManager.get("card.name.nuke"))
				.andReturn("Nuke").anyTimes();
		EasyMock.expect(localeManager.get("card.name.reverse"))
				.andReturn("Reverse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.swap_top_and_bottom"))
				.andReturn("Swap Top and Bottom").anyTimes();
		EasyMock.expect(localeManager.get("card.name.favor"))
				.andReturn("Favor").anyTimes();
		EasyMock.expect(localeManager.get("card.name.unknown_card_for_test"))
				.andReturn("").anyTimes();
		EasyMock.replay(localeManager);


		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("YOUR HAND (0 cards):"));
		assertTrue(out.contains("(empty hand)"));

		for (CardType type : CardType.values()) {
			String formattedName = ui.formatCardName(type);
			assertFalse(out.contains(formattedName),
					"Should not display " +
							formattedName + " when player has none");
		}
	}

	@Test
	public void displayPlayerHand_countIntegerNullHandling_specific() {
		UserInterface ui = new UserInterface(localeManager);

		Hand emptyHand = new Hand();
		Player playerWithEmptyHand = new Player(emptyHand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.empty"))
				.andReturn("(empty hand)");
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(playerWithEmptyHand);
		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("(empty hand)"));

		assertFalse(out.contains(" x0"), "Should never show x0 for any card");
		assertFalse(out.contains("Skip"), "Should not show Skip when count is null/0");
		assertFalse(out.contains("Attack"), "Should not show Attack when count is null/0");
	}

	@Test
	public void displayPlayerHand_ternaryOperatorBehavior() {
		UserInterface ui = new UserInterface(localeManager);
		Hand hand = new Hand();
		hand.addCard(new SkipCard());
		Player player = new Player(hand);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type").anyTimes();
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(player);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Skip (type: skip)"));
		assertFalse(out.contains("Attack"),
				"Should not show Attack when player doesn't have any");
		assertFalse(out.contains("Defuse"),
				"Should not show Defuse when player doesn't have any");
		assertFalse(out.contains(" x0"),
				"Cards with count 0 should not be displayed");
	}

	@Test
	public void displayPlayerHand_nullCountHandling_PrintsCorrectHand() {
		UserInterface ui = new UserInterface(localeManager);

		final int NUMBER_OF_CARDS = 1;
		final int NO_CARDS = 0;

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.getNumberOfCards()).andReturn(
				NUMBER_OF_CARDS)
				.anyTimes();
		EasyMock.expect(mockPlayer.getCardTypeCount(CardType.SKIP))
				.andReturn(null);
		EasyMock.expect(mockPlayer.getCardTypeCount(CardType.ATTACK))
				.andReturn(NUMBER_OF_CARDS);
		for (CardType type : CardType.values()) {
			if (type != CardType.SKIP && type != CardType.ATTACK) {
				EasyMock.expect(mockPlayer.getCardTypeCount(type))
						.andReturn(NO_CARDS);
			}
		}
		EasyMock.replay(mockPlayer);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type");
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(mockPlayer);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Attack (type: attack)"));
		assertFalse(out.contains("Skip"));
		assertFalse(out.contains("Defuse"));
		assertFalse(out.contains("Favor"));

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void displayPlayerHand_nullCardType_printsCorrectHand() {
		UserInterface ui = new UserInterface(localeManager);
		final int NUMBER_OF_CARDS = 2;
		final int NO_CARDS = 0;
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.getNumberOfCards())
				.andReturn(NUMBER_OF_CARDS).anyTimes();
		EasyMock.expect(mockPlayer.getCardTypeCount(CardType.SKIP))
				.andReturn(null);
		EasyMock.expect(mockPlayer.getCardTypeCount(CardType.ATTACK))
				.andReturn(0);
		EasyMock.expect(mockPlayer.getCardTypeCount(CardType.DEFUSE))
				.andReturn(NUMBER_OF_CARDS);
		for (CardType type : CardType.values()) {
			if (type != CardType.SKIP && type != CardType.ATTACK
					&& type != CardType.DEFUSE) {
				EasyMock.expect(mockPlayer.getCardTypeCount(type))
						.andReturn(NO_CARDS);
			}
		}
		EasyMock.replay(mockPlayer);

		EasyMock.expect(localeManager.get("hand.title"))
				.andReturn("YOUR HAND");
		EasyMock.expect(localeManager.get("hand.cards"))
				.andReturn("cards");
		EasyMock.expect(localeManager.get("hand.type"))
				.andReturn("type");
		EasyMock.expect(localeManager.get("hand.usage"))
				.andReturn("Use 'play <type>' to play a card (e.g., 'play skip')");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip");
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack");
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.expect(localeManager.get("card.name.defuse"))
				.andReturn("Defuse").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayPlayerHand(mockPlayer);

		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("Defuse x2 (type: defuse)"));
		assertFalse(out.contains("Skip"));
		assertFalse(out.contains("Attack"));

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void getUserInput_withNullMessageAndNonEmptyConsoleInput_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		String result = ui.getUserInput(null);
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getUserInput_withEmptyMessageAndNonEmptyConsoleInput_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		String emptyMessage = "";
		String result = ui.getUserInput(emptyMessage);
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getUserInput_withNonEmptyMessageAndEmptyConsoleInput_keepsAskingForInput() {
		String input = String.join("\n", "", "hello world");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));

		UserInterface ui = new UserInterface(localeManager);
		String message = "message";
		String result = ui.getUserInput(message);

		assertEquals("hello world", result);

		String output = outContent.toString(StandardCharsets.UTF_8);
		int promptCount = output.split("> ", -1).length - 1;
		final int EXPECTED_PROMPTS = 2;
		assertEquals(EXPECTED_PROMPTS, promptCount);
	}

	@Test
	public void getUserInput_withValidMessageAndInput_returnsConsoleInputAndPrintsMessage() {
		System.setIn(new ByteArrayInputStream("hello world\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		String message = "message";
		String result = ui.getUserInput(message);
		assertEquals("hello world", result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains(message));
	}

	@Test
	public void getNumericUserInput_withNullMessage_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("1\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error.limit.number"))
				.andReturn("Please enter a number between %d and %d.");
		EasyMock.replay(localeManager);

		int result = ui.getNumericUserInput(null, 0, 1);
		assertEquals(1, result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getNumericUserInput_withEmptyMessage_returnsConsoleInput() {
		System.setIn(new ByteArrayInputStream("1\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error.limit.number"))
				.andReturn("Please enter a number between %d and %d.");
		EasyMock.replay(localeManager);

		String emptyMessage = "";
		int result = ui.getNumericUserInput(emptyMessage, 0, 2);
		assertEquals(1, result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
	}

	@Test
	public void getNumericUserInput_withNonNumericConsoleInput_keepsAskingForInput() {
		String input = String.join("\n", "hello world", "0");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));

		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error.limit.number"))
				.andReturn("Please enter a number between %d and %d.")
				.anyTimes();
		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ").anyTimes();
		EasyMock.replay(localeManager);

		String message = "message";
		int result = ui.getNumericUserInput(message, 0, 1);

		assertEquals(0, result);

		String output = outContent.toString(StandardCharsets.UTF_8);
		int promptCount = output.split("> ", -1).length - 1;
		final int EXPECTED_PROMPTS = 2;
		assertEquals(EXPECTED_PROMPTS, promptCount);
	}

	@Test
	public void getNumericUserInput_withEmptyConsoleInput_keepsAskingForInput() {
		String input = String.join("\n", "", "2");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));

		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error.limit.number"))
				.andReturn("Please enter a number between %d and %d.")
				.anyTimes();
		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ").anyTimes();
		EasyMock.replay(localeManager);

		String message = "message";
		final int maxBasedOnMaxNumberOfPlayers = 4;
		int result = ui.getNumericUserInput(message, 0, maxBasedOnMaxNumberOfPlayers);

		assertEquals(2, result);

		String output = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(output.contains(message));
		int promptCount = output.split("> ", -1).length - 1;
		final int EXPECTED_PROMPTS = 2;
		assertEquals(EXPECTED_PROMPTS, promptCount);
	}

	@Test
	public void getNumericUserInput_withIntegerInput_returnsConsoleInputAndPrintsMessage() {
		System.setIn(new ByteArrayInputStream("2\n"
				.getBytes(StandardCharsets.UTF_8)));
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error.limit.number"))
				.andReturn("Please enter a number between %d and %d.");
		EasyMock.replay(localeManager);

		String message = "message";
		int result = ui.getNumericUserInput(message, 1, 2);
		assertEquals(2, result);
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains("> "));
		assertTrue(outContent.toString(StandardCharsets.UTF_8).contains(message));
	}

	@Test
	public void getNumericUserInput_withInputMoreThanMax_keepsAskingForInput() {
		String input = String.join("\n", "3", "2");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));

		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error.limit.number"))
				.andReturn("Please enter a number between %d and %d.")
				.anyTimes();
		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ").anyTimes();
		EasyMock.replay(localeManager);

		String message = "message";
		int result = ui.getNumericUserInput(message, 1, 2);

		assertEquals(2, result);

		String output = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(output.contains(message));
		int promptCount = output.split("> ", -1).length - 1;
		final int EXPECTED_PROMPTS = 2;
		assertEquals(EXPECTED_PROMPTS, promptCount);
	}

	@Test
	public void getNumericUserInput_withInputLessThanMin_keepsAskingForInput() {
		String input = String.join("\n", "0", "1");
		System.setIn(new ByteArrayInputStream((input + "\n")
				.getBytes(StandardCharsets.UTF_8)));

		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("error.limit.number"))
				.andReturn("Please enter a number between %d and %d.")
				.anyTimes();
		EasyMock.expect(localeManager.get("error"))
				.andReturn("Error: ").anyTimes();
		EasyMock.replay(localeManager);

		String message = "message";
		int maxIndexForMaxPlayers = MAX_PLAYERS - 1;
		int result = ui.getNumericUserInput(message, 1, maxIndexForMaxPlayers);

		assertEquals(1, result);

		String output = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(output.contains(message));
		int promptCount = output.split("> ", -1).length - 1;
		final int EXPECTED_PROMPTS = 2;
		assertEquals(EXPECTED_PROMPTS, promptCount);
	}

	@Test
	public void displayCardsFromDeck_withEmptyCards_printsNoCardsMessage() {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("no.cards.view"))
				.andReturn("No cards to view");
		EasyMock.replay(localeManager);

		List<Card> emptyCards = new ArrayList<>();

		ui.displayCardsFromDeck(emptyCards, 1);
		String out = outContent.toString(StandardCharsets.UTF_8);
		assertTrue(out.contains("No cards to view"));
		assertFalse(out.contains(":Top of deck:"));
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void displayCardsFromDeck_withNegativeDeckSize_throwsIllegalArgumentException(
			CardType testCardType) {
		UserInterface ui = new UserInterface(localeManager);

		Card testCard = mockCard(testCardType);
		List<Card> oneCardList = new ArrayList<>(List.of(testCard));

		String expectedMessage = "deckSize can not be negative";
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> ui.displayCardsFromDeck(oneCardList, -1));
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void displayCardsFromDeck_withOneCardAndDeckSizeZero_throwsIllegalArgumentException(
			CardType testCardType
	) {
		UserInterface ui = new UserInterface(localeManager);

		Card testCard = mockCard(testCardType);
		List<Card> oneCardList = new ArrayList<>(List.of(testCard));

		String expectedMessage = "deckSize is less than number of cards to display";
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> ui.displayCardsFromDeck(oneCardList, 0));
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void displayCardsFromDeck_withOneCard_printCardTypeAndIndex(
			CardType testCardType
	) {
		UserInterface ui = new UserInterface(localeManager);

		EasyMock.expect(localeManager.get("card.name.normal"))
				.andReturn("Normal Cat").anyTimes();
		EasyMock.expect(localeManager.get("card.name.exploding_kitten"))
				.andReturn("Exploding Kitten").anyTimes();
		EasyMock.expect(localeManager.get("card.name.defuse"))
				.andReturn("Defuse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.attack"))
				.andReturn("Attack").anyTimes();
		EasyMock.expect(localeManager.get("card.name.skip"))
				.andReturn("Skip").anyTimes();
		EasyMock.expect(localeManager.get("card.name.favor"))
				.andReturn("Favor").anyTimes();
		EasyMock.expect(localeManager.get("card.name.shuffle"))
				.andReturn("Shuffle").anyTimes();
		EasyMock.expect(localeManager.get("card.name.see_the_future"))
				.andReturn("See the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.alter_the_future"))
				.andReturn("Alter the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.nuke"))
				.andReturn("Nuke").anyTimes();
		EasyMock.expect(localeManager.get("card.name.reverse"))
				.andReturn("Reverse").anyTimes();
		EasyMock.expect(localeManager.get("card.name.swap_top_and_bottom"))
				.andReturn("Swap Top and Bottom").anyTimes();
		EasyMock.expect(localeManager.get("card.name.unknown_card_for_test"))
				.andReturn("").anyTimes();
		EasyMock.expect(localeManager.get("deck.view.top"))
				.andReturn(":Top of deck:");
		EasyMock.expect(localeManager.get("deck.view.entry"))
				.andReturn("%s, index: %d").anyTimes();
		EasyMock.replay(localeManager);

		Card testCard = mockCard(testCardType);
		List<Card> oneCardList = new ArrayList<>(List.of(testCard));

		ui.displayCardsFromDeck(oneCardList, 1);
		String out = outContent.toString(StandardCharsets.UTF_8);
		String expectedCardInfo = ui.formatCardName(testCardType) + ", index: 0";

		assertFalse(out.contains("No cards to view"));
		assertTrue(out.contains(expectedCardInfo));
		assertTrue(out.contains(":Top of deck:"));
	}

	@Test
	public void displayCardsFromDeck_withTwoCardsAndDeckSizeOne_throwsIllegalArgumentException()
	{
		UserInterface ui = new UserInterface(localeManager);

		Card testCard1 = mockCard(CardType.NORMAL);
		Card testCard2 = mockCard(CardType.ALTER_THE_FUTURE);
		List<Card> twoCardList = new ArrayList<>(List.of(testCard1, testCard2));

		String expectedMessage = "deckSize is less than number of cards to display";
		Exception exception = assertThrows(
				IllegalArgumentException.class,
				() -> ui.displayCardsFromDeck(twoCardList, 1));
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void displayCardsFromDeck_withTwoCards_printCardTypeAndIndex() {
		UserInterface ui = new UserInterface(localeManager);

		Card testCard1 = mockCard(CardType.NORMAL);
		Card testCard2 = mockCard(CardType.ALTER_THE_FUTURE);

		List<Card> twoCardList = new ArrayList<>
				(List.of(testCard1, testCard2));

		EasyMock.expect(localeManager.get("card.name.normal"))
				.andReturn("Normal Cat").anyTimes();
		EasyMock.expect(localeManager.get("card.name.alter_the_future"))
				.andReturn("Alter the Future").anyTimes();
		EasyMock.expect(localeManager.get("deck.view.top"))
				.andReturn(":Top of deck:");
		EasyMock.expect(localeManager.get("deck.view.entry"))
				.andReturn("%s, index: %d").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayCardsFromDeck(twoCardList, 2);
		String out = outContent.toString(StandardCharsets.UTF_8);
		String expectedCardInfo =
				"\n:Top of deck:\n" +
				"Normal Cat, index: 1\n" +
				"Alter the Future, index: 0";

		assertFalse(out.contains("No cards to view"));
		assertTrue(out.contains(expectedCardInfo));
	}

	@Test
	public void displayCardsFromDeck_withThreeCardsAndDuplicate_printCardTypeAndIndex()
	{
		UserInterface ui = new UserInterface(localeManager);

		Card testCard1 = mockCard(CardType.SEE_THE_FUTURE);
		Card testCard2 = mockCard(CardType.EXPLODING_KITTEN);
		Card testCard3 = mockCard(CardType.EXPLODING_KITTEN);

		List<Card> threeCardList = new ArrayList<>
				(List.of(testCard1, testCard2, testCard3));

		int deckSizeOneMoreThanCardsSize = threeCardList.size() + 1;

		EasyMock.expect(localeManager.get("card.name.see_the_future"))
				.andReturn("See the Future").anyTimes();
		EasyMock.expect(localeManager.get("card.name.exploding_kitten"))
				.andReturn("Exploding Kitten").anyTimes();
		EasyMock.expect(localeManager.get("deck.view.top"))
				.andReturn(":Top of deck:");
		EasyMock.expect(localeManager.get("deck.view.entry"))
				.andReturn("%s, index: %d").anyTimes();
		EasyMock.replay(localeManager);

		ui.displayCardsFromDeck(threeCardList, deckSizeOneMoreThanCardsSize);

		String out = outContent.toString(StandardCharsets.UTF_8);
		String expectedCardInfo =
				"\n:Top of deck:\n" +
						"See the Future, index: 3\n" +
						"Exploding Kitten, index: 2\n" +
						"Exploding Kitten, index: 1";

		assertFalse(out.contains("No cards to view"));
		assertTrue(out.contains(expectedCardInfo));
	}

	private Card mockCard(CardType cardType) {
		Card mockCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
		EasyMock.replay(mockCard);
		return mockCard;
	}
}
