package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;
import ui.UserInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineLoopTest {
	private static final int ONE_CYCLE = 1;
	private static final int INITIAL_HAND_SIZE = 4;
	private static final int DECK_SIZE = 10;

	private static class TestableGameEngine extends GameEngine {
		public int eliminationCount = 0;
		public int displayCount = 0;
		public int processCount = 0;
		public int checkCount = 0;

		public TestableGameEngine(
				TurnManager turnManager,
				PlayerManager playerManager,
				Deck deck,
				UserInterface userInterface,
				CardFactory cardFactory,
				SecureRandom secureRandom
		) {
			super(turnManager, playerManager, deck, userInterface, cardFactory,
					secureRandom);
		}

		@Override
		public void handlePlayerGetsEliminated() {
			eliminationCount++;
		}

		@Override
		public void displayGameState(Player currentPlayer) {
			displayCount++;
		}

		@Override
		public void processCommand(String input, Player currentPlayer) {
			processCount++;
		}

		@Override
		public void checkWinCondition() {
			checkCount++;
			setGameRunning(false);
		}

		public void invokeRunGameLoop() throws Exception {
			Method runLoop = GameEngine.class.getDeclaredMethod("runGameLoop");
			runLoop.setAccessible(true);
			runLoop.invoke(this);
		}
	}

	private static class TestableMainEngine extends GameEngine {
		public boolean initCalled = false;
		public boolean loopCalled = false;

		public TestableMainEngine(
				TurnManager turnManager,
				PlayerManager playerManager,
				Deck deck,
				UserInterface userInterface,
				CardFactory cardFactory,
				SecureRandom secureRandom
		) {
			super(turnManager, playerManager, deck,
					userInterface, cardFactory, secureRandom);
		}

		@Override
		public void initializeGame() {
			initCalled = true;
		}

		@Override
		public void runGameLoop() {
			loopCalled = true;
		}
	}

	private TurnManager mockTurnManager;
	private UserInterface mockUI;
	private PlayerManager mockPlayerManager;
	private Deck mockDeck;
	private CardFactory mockFactory;
	private SecureRandom mockRandom;

	@BeforeEach
	public void setUp() {
		mockTurnManager = EasyMock.createMock(TurnManager.class);
		mockUI = EasyMock.createMock(UserInterface.class);
		mockPlayerManager = EasyMock.createMock(PlayerManager.class);
		mockDeck = EasyMock.createMock(Deck.class);
		mockFactory = EasyMock.createMock(CardFactory.class);
		mockRandom = EasyMock.createMock(SecureRandom.class);
	}

	@Test
	public void runGameLoop_withEliminationThenQuit_invokesCorrectMethods()
			throws Exception {
		Player eliminated = EasyMock.createMock(Player.class);
		Player active = EasyMock.createMock(Player.class);

		EasyMock.expect(eliminated.isInGame()).andReturn(false);
		EasyMock.expect(active.isInGame()).andReturn(true);
		EasyMock.expect(mockTurnManager.getCurrentActivePlayer())
				.andReturn(eliminated).andReturn(active);
		EasyMock.expect(mockUI.getUserInput()).andReturn("quit");

		EasyMock.replay(mockTurnManager, mockUI, eliminated, active);

		TestableGameEngine engine = new TestableGameEngine(
				mockTurnManager, mockPlayerManager, mockDeck, mockUI,
				mockFactory, mockRandom
		);

		engine.invokeRunGameLoop();

		assertEquals(ONE_CYCLE, engine.eliminationCount);
		assertEquals(ONE_CYCLE, engine.displayCount);
		assertEquals(ONE_CYCLE, engine.processCount);
		assertEquals(ONE_CYCLE, engine.checkCount);

		EasyMock.verify(mockTurnManager, mockUI, eliminated, active);
	}

	@Test
	public void runGameLoop_withoutElimination_invokesDisplayAndProcessThenQuit
			() throws Exception {
		Player active = EasyMock.createMock(Player.class);

		EasyMock.expect(active.isInGame()).andReturn(true);
		EasyMock.expect(mockTurnManager.getCurrentActivePlayer())
				.andReturn(active);
		EasyMock.expect(mockUI.getUserInput()).andReturn("quit");

		EasyMock.replay(mockTurnManager, mockUI, active);

		TestableGameEngine engine = new TestableGameEngine(
				mockTurnManager, mockPlayerManager, mockDeck, mockUI,
				mockFactory, mockRandom
		);

		engine.invokeRunGameLoop();

		assertEquals(0, engine.eliminationCount);
		assertEquals(ONE_CYCLE, engine.displayCount);
		assertEquals(ONE_CYCLE, engine.processCount);
		assertEquals(ONE_CYCLE, engine.checkCount);

		EasyMock.verify(mockTurnManager, mockUI, active);
	}

	@Test
	public void initializeGame_dealsCardsAddsDefuseInsertsExplodingKittensAndShowsHelp
			() {
		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);
		List<Player> players = List.of(player1, player2);
		int playerCount = players.size();
		int explodingCount = playerCount - ONE_CYCLE;

		EasyMock.expect(mockPlayerManager.getPlayers()).andReturn(players);

		player1.drawCard(mockDeck);
		EasyMock.expectLastCall().times(INITIAL_HAND_SIZE);
		player2.drawCard(mockDeck);
		EasyMock.expectLastCall().times(INITIAL_HAND_SIZE);

		Card defuseCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockFactory.createCard(CardType.DEFUSE))
				.andReturn(defuseCard).times(playerCount);

		player1.addCardToHand(defuseCard);
		EasyMock.expectLastCall();
		player2.addCardToHand(defuseCard);
		EasyMock.expectLastCall();

		EasyMock.expect(mockDeck.getDeckSize()).andReturn(DECK_SIZE);
		Card kittenCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockFactory.createCard(CardType.EXPLODING_KITTEN))
				.andReturn(kittenCard).times(explodingCount);

		mockDeck.insertCardAt(EasyMock.eq(kittenCard), EasyMock.anyInt());
		EasyMock.expectLastCall().times(explodingCount);
		mockUI.displayHelp();
		EasyMock.expectLastCall();

		EasyMock.replay(mockPlayerManager, mockDeck, player1, player2,
				mockFactory, mockUI);

		TestableGameEngine engine = new TestableGameEngine(
				mockTurnManager, mockPlayerManager, mockDeck,
				mockUI, mockFactory, mockRandom
		);

		engine.initializeGame();

		assertTrue(engine.getIsGameRunning());

		EasyMock.verify(mockPlayerManager, mockDeck, player1, player2,
				mockFactory, mockUI);
	}


	@Test
	public void gameFlow_successPath_callsInitializeAndLoop() {
		TestableMainEngine engine = new TestableMainEngine(
				mockTurnManager, mockPlayerManager, mockDeck,
				mockUI, mockFactory, mockRandom
		);
		engine.initializeGame();
		engine.runGameLoop();

		assertTrue(engine.initCalled,
				"initializeGame() should have been called");
		assertTrue(engine.loopCalled,
				"runGameLoop() should have been called");
	}

	@Test
	public void gameFlow_initializeThrows_handlesException() {
		TestableMainEngine engine = new TestableMainEngine(
				mockTurnManager, mockPlayerManager, mockDeck,
				mockUI, mockFactory, mockRandom
		) {
			@Override
			public void initializeGame() {
				throw new RuntimeException("init failed");
			}
		};

		RuntimeException exception = assertThrows
				(RuntimeException.class, engine::initializeGame);

		assertEquals("init failed", exception.getMessage());
		assertFalse(engine.loopCalled,
				"runGameLoop() " +
						"should not be called when " +
						"initializeGame() throws");
	}

	@Test
	public void main_runsGameSuccessfully() {
		InputStream originalIn = System.in;
		PrintStream originalOut = System.out;

		try {
			String input = "2\nquit\n";
			System.setIn(new ByteArrayInputStream
					(input.getBytes(StandardCharsets.UTF_8)));

			ByteArrayOutputStream outputCapture =
					new ByteArrayOutputStream();
			System.setOut(new PrintStream(outputCapture,
					true, StandardCharsets.UTF_8));

			assertDoesNotThrow(() -> {
				GameEngine.main(new String[]{});
			});

			String output = outputCapture.toString(StandardCharsets.UTF_8);
			assertTrue(
					output.contains("Welcome") ||
							output.contains("players"),
					"Expected welcome message not found");
			assertTrue(output.contains
							("Thanks for playing") ||
							output.contains("quit"),
					"Expected quit message " +
							"not found");

		} finally {
			System.setIn(originalIn);
			System.setOut(originalOut);
		}
	}
}