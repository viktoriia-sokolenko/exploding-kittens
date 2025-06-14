package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;
import ui.UserInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
			super(turnManager, playerManager, deck, userInterface, cardFactory, secureRandom);
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
		mockTurnManager   = EasyMock.createMock(TurnManager.class);
		mockUI            = EasyMock.createMock(UserInterface.class);
		mockPlayerManager = EasyMock.createMock(PlayerManager.class);
		mockDeck          = EasyMock.createMock(Deck.class);
		mockFactory       = EasyMock.createMock(CardFactory.class);
		mockRandom        = EasyMock.createMock(SecureRandom.class);
	}

	@Test
	public void runGameLoop_withEliminationThenQuit_invokesCorrectMethods()
			throws Exception {
		Player elim   = EasyMock.createMock(Player.class);
		Player active = EasyMock.createMock(Player.class);

		EasyMock.expect(elim.isInGame()).andReturn(false);
		EasyMock.expect(active.isInGame()).andReturn(true);
		EasyMock.expect(mockTurnManager.getCurrentActivePlayer())
				.andReturn(elim).andReturn(active);
		EasyMock.expect(mockUI.getUserInput()).andReturn("quit");

		EasyMock.replay(mockTurnManager, mockUI, elim, active);

		TestableGameEngine engine = new TestableGameEngine(
				mockTurnManager, mockPlayerManager, mockDeck, mockUI,
				mockFactory, mockRandom
		);

		engine.invokeRunGameLoop();

		assertEquals(ONE_CYCLE, engine.eliminationCount);
		assertEquals(ONE_CYCLE, engine.displayCount);
		assertEquals(ONE_CYCLE, engine.processCount);
		assertEquals(ONE_CYCLE, engine.checkCount);

		EasyMock.verify(mockTurnManager, mockUI, elim, active);
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
	public
	void initializeGame_dealsCardsAddsDefuseInsertsExplodingKittensAndShowsHelp
			() {
		Player p1 = EasyMock.createMock(Player.class);
		Player p2 = EasyMock.createMock(Player.class);
		List<Player> players = List.of(p1, p2);
		int playerCount   = players.size();
		int explodingCount = playerCount - ONE_CYCLE;

		EasyMock.expect(mockPlayerManager.getPlayers()).andReturn(players);

		p1.drawCard(mockDeck);
		EasyMock.expectLastCall().times(INITIAL_HAND_SIZE);
		p2.drawCard(mockDeck);
		EasyMock.expectLastCall().times(INITIAL_HAND_SIZE);

		Card defuseCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockFactory.createCard(CardType.DEFUSE))
				.andReturn(defuseCard).times(playerCount);

		p1.addCardToHand(defuseCard);
		EasyMock.expectLastCall();
		p2.addCardToHand(defuseCard);
		EasyMock.expectLastCall();

		EasyMock.expect(mockDeck.getDeckSize()).andReturn(DECK_SIZE);
		Card kittenCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockFactory.createCard(CardType.EXPLODING_KITTEN))
				.andReturn(kittenCard).times(explodingCount);

		mockDeck.insertCardAt(EasyMock.eq(kittenCard), EasyMock.anyInt());
		EasyMock.expectLastCall().times(explodingCount);
		mockUI.displayHelp();
		EasyMock.expectLastCall();

		EasyMock.replay(mockPlayerManager, mockDeck, p1, p2,
				mockFactory, mockUI);

		TestableGameEngine engine = new TestableGameEngine(
				mockTurnManager, mockPlayerManager, mockDeck,
				mockUI, mockFactory, mockRandom
		);

		engine.initializeGame();

		assertTrue(engine.getIsGameRunning());

		EasyMock.verify(mockPlayerManager, mockDeck, p1, p2,
				mockFactory, mockUI);
	}
	@Test
	public void main_successPath_callsInitializeAndLoopAndNoError()
			throws Exception {
		System.setIn(new ByteArrayInputStream("2\n"
				.getBytes(StandardCharsets.UTF_8)));
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		EasyMock.replay(mockUI);

		TestableMainEngine engine = new TestableMainEngine(
				mockTurnManager, mockPlayerManager, mockDeck,
				mockUI, mockFactory, mockRandom
		);

		

		getMainMethod().invoke(engine, (Object) new String[] {});

		assertTrue(engine.initCalled,
				"initializeGame() should have been called");
		assertTrue(engine.loopCalled,
				"runGameLoop() should have been called");

		EasyMock.verify(mockUI);
	}



	private Method getMainMethod() throws Exception {
		Method m = GameEngine.class.getDeclaredMethod(
				"main", String[].class);
		m.setAccessible(true);
		return m;
	}

}
