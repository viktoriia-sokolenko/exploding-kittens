package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.UserInterface;

import static org.junit.jupiter.api.Assertions.*;

public class GameContextTest {
	private GameContext gameContext;
	private Player mockCurrentPlayer;
	private TurnManager mockTurnManager;
	private PlayerManager mockPlayerManager;
	private Deck mockDeck;
	private UserInterface userInterface;


	@BeforeEach
	public void setUp() {
		mockCurrentPlayer = EasyMock.createMock(Player.class);
		gameContext = new GameContext(mockCurrentPlayer);
		mockTurnManager = EasyMock.createMock(TurnManager.class);
		mockDeck = EasyMock.createMock(Deck.class);
		mockPlayerManager = EasyMock.createMock(PlayerManager.class);
		userInterface = EasyMock.createMock(UserInterface.class);
	}

	@Test
	void constructor_withValidPlayer_createsGameContext() {
		assertNotNull(gameContext);
	}

	@Test
	void constructor_withNullPlayer_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(null);
		});
	}

	@Test
	void getCurrentPlayer_returnsCurrentPlayer() {
		Player result = gameContext.getCurrentPlayer();
		assertEquals(mockCurrentPlayer, result);
	}

	// TODO: Don't do anything, but just a placeholder for now
	@Test
	void endTurnWithoutDrawing_doesNotThrowException() {
		assertDoesNotThrow(() -> gameContext.endTurnWithoutDrawing());
	}

	@Test
	void endTurnWithoutDrawingForAttacks_doesNotThrowException() {
		assertDoesNotThrow(() -> gameContext.endTurnWithoutDrawingForAttacks());
	}

	@Test
	void constructor_withValidParameters_createsGameContext() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface);
		assertNotNull(fullGameContext);
		assertEquals(mockCurrentPlayer, fullGameContext.getCurrentPlayer());
	}

	@Test
	void constructor_withNullTurnManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(null, mockPlayerManager,
					mockDeck, mockCurrentPlayer,
					userInterface);
		});
	}

	@Test
	void constructor_withNullPlayerManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, null, mockDeck,
					mockCurrentPlayer, userInterface);
		});
	}

	@Test
	void constructor_withNullDeck_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, mockPlayerManager, null,
					mockCurrentPlayer, userInterface);
		});
	}

	@Test
	void constructor_withNullCurrentPlayer_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, mockPlayerManager, mockDeck,
					null, userInterface);
		});
	}

	@Test
	void fullConstructor_withNullUI_allowsNullUI() {
		assertDoesNotThrow(() -> {
			new GameContext(mockTurnManager, mockPlayerManager, mockDeck,
					mockCurrentPlayer, null);
		});
	}

	@Test
	void addTurnForCurrentPlayer_withFullContext_callsTurnManager() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface);

		mockTurnManager.addTurnForCurrentPlayer();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockTurnManager);

		fullGameContext.addTurnForCurrentPlayer();

		EasyMock.verify(mockTurnManager);
	}

	@Test
	void addTurnForCurrentPlayer_withSimpleContext_doesNotThrowException() {
		assertDoesNotThrow(() -> gameContext.addTurnForCurrentPlayer());
	}

	@Test
	void endTurnWithoutDrawing_withFullContext_callsTurnManager() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface);

		mockTurnManager.endTurnWithoutDraw();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockTurnManager);

		fullGameContext.endTurnWithoutDrawing();

		EasyMock.verify(mockTurnManager);
	}

	@Test
	void endTurnWithoutDrawingForAttacks_withFullContext_callsTurnManager() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface);

		mockTurnManager.endTurnWithoutDrawForAttacks();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockTurnManager);

		fullGameContext.endTurnWithoutDrawingForAttacks();

		EasyMock.verify(mockTurnManager);
	}

}
