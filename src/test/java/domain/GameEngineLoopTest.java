package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;
import ui.UserInterface;

import java.lang.reflect.Method;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineLoopTest {
    private static final int ONE_CYCLE = 1;

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
            super(turnManager, playerManager, deck, userInterface, cardFactory, secureRandom);
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
    public void runGameLoop_withEliminationThenQuit_invokesCorrectMethods
            () throws Exception {
        Player elim = EasyMock.createMock(Player.class);
        Player active = EasyMock.createMock(Player.class);

        EasyMock.expect(elim.isInGame()).andReturn(false);
        EasyMock.expect(active.isInGame()).andReturn(true);
        EasyMock.expect(mockTurnManager.getCurrentActivePlayer())
                .andReturn(elim).andReturn(active);
        EasyMock.expect(mockUI.getUserInput()).andReturn("quit");

        EasyMock.replay(mockTurnManager, mockUI, elim, active);

        TestableGameEngine engine = new TestableGameEngine(
                mockTurnManager, mockPlayerManager, mockDeck, mockUI, mockFactory, mockRandom
        );

        engine.invokeRunGameLoop();

        assertEquals(ONE_CYCLE, engine.eliminationCount);
        assertEquals(ONE_CYCLE, engine.displayCount);
        assertEquals(ONE_CYCLE, engine.processCount);
        assertEquals(ONE_CYCLE, engine.checkCount);

        EasyMock.verify(mockTurnManager, mockUI, elim, active);
    }
}
