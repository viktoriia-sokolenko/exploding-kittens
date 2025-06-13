package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ui.UserInterface;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
	private GameEngine gameEngine;
	private TurnManager mockTurnManager;
	private PlayerManager mockPlayerManager;
	private UserInterface mockUserInterface;
	private Deck mockDeck;
	private CardFactory mockCardFactory;

	@BeforeEach
	public void setUp() {
		mockTurnManager = EasyMock.createMock(TurnManager.class);
		mockPlayerManager = EasyMock.createMock(PlayerManager.class);
		mockUserInterface = EasyMock.createMock(UserInterface.class);
		mockDeck = EasyMock.createMock(Deck.class);
		mockCardFactory = EasyMock.createMock(CardFactory.class);

		gameEngine = new GameEngine(
				mockTurnManager,
				mockPlayerManager,
				mockDeck,
				mockUserInterface,
				mockCardFactory
		);
	}

	private GameEngine createValidGameEngine() {
		return new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory);
	}

	private Player createMockPlayer() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.getNumberOfCards()).andStubReturn(5);
		EasyMock.expect(mockPlayer.isInGame()).andStubReturn(true);
		EasyMock.replay(mockPlayer);
		return mockPlayer;
	}

	private Card createMockCard(CardType cardType) {
		Card mockCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
		EasyMock.replay(mockCard);
		return mockCard;
	}

	private void setupMocksForStartGame() {
		List<Player> players = Arrays.asList(createMockPlayer(), createMockPlayer());
		EasyMock.expect(mockPlayerManager.getPlayers()).andReturn(players);

		for (Player player : players) {
			for (int i = 0; i < 4; i++) {
				player.drawCard(mockDeck);
				EasyMock.expectLastCall();
			}

		}
		for (int i = 0; i < players.size() - 1; i++) {
			EasyMock.expect(mockCardFactory.createCard(CardType.EXPLODING_KITTEN))
					.andReturn(createMockCard(CardType.EXPLODING_KITTEN));
			EasyMock.expect(mockDeck.getDeckSize()).andReturn(10);
			mockDeck.insertCardAt(EasyMock.anyObject(), EasyMock.anyInt());
			EasyMock.expectLastCall();
		}

		EasyMock.expect(mockCardFactory.createCard(CardType.DEFUSE))
				.andReturn(createMockCard(CardType.DEFUSE)).times(players.size());

		mockUserInterface.displayHelp();
		EasyMock.expectLastCall();

		EasyMock.replay(mockPlayerManager, mockDeck, mockCardFactory, mockUserInterface);
	}

	@Test
	public void constructor_withNullTurnManager_throwsNullPointerException() {
		NullPointerException thrown = assertThrows(
				NullPointerException.class,
				() -> new GameEngine(
						null,
						mockPlayerManager,
						mockDeck,
						mockUserInterface,
						mockCardFactory
				)
		);
		assertEquals("turnManager must not be null",
				thrown.getMessage());
	}

	@Test
	void constructor_withNullPlayerManager_throwsNullPointerException() {
		NullPointerException ex = assertThrows(
				NullPointerException.class,
				() -> new GameEngine(
						mockTurnManager,
						null,
						mockDeck,
						mockUserInterface,
						mockCardFactory
				)
		);
		assertEquals("playerManager must not be null", ex.getMessage());
	}

	@Test
	void constructor_withNullDeck_throwsNullPointerException() {
		NullPointerException ex = assertThrows(
				NullPointerException.class,
				() -> new GameEngine(
						mockTurnManager,
						mockPlayerManager,
						null,
						mockUserInterface,
						mockCardFactory
				)
		);
		assertEquals("deck must not be null", ex.getMessage());
	}

	@Test
	void constructor_withNullUI_allowsNullUI() {
		assertDoesNotThrow(() -> new GameEngine(
				mockTurnManager,
				mockPlayerManager,
				mockDeck,
				null,
				mockCardFactory
		));
	}

	@Test
	void constructor_withNullCardFactory_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () ->
				new GameEngine(
						mockTurnManager,
						mockPlayerManager,
						mockDeck,
						mockUserInterface,
						null));
	}

	@Test
	void playCard_withNullPlayer_throwsNullPointerException() {
		SkipCard skipCard = new SkipCard();

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.playCard(null, skipCard)
		);

		assertEquals("Player cannot be null", exception.getMessage());
	}

	@Test
	void playCard_withNullCard_throwsNullPointerException() {
		Hand hand	= new Hand();
		Player player = new Player(hand);

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.playCard(player, null)
		);

		assertEquals("Card cannot be null", exception.getMessage());
	}

	@Test
	void playCard_playerHasCard_executesCardEffect() {
		Hand hand	= new Hand();
		Player player = new Player(hand);
		SkipCard skipCard = new SkipCard();

		hand.addCard(skipCard);
		assertEquals(1, player.getCardTypeCount(CardType.SKIP));

		mockTurnManager.endTurnWithoutDraw();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockTurnManager);

		assertDoesNotThrow(() -> gameEngine.playCard(player, skipCard));
		assertEquals(0, player.getCardTypeCount(CardType.SKIP));

		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void createNewGame_createsValidGameEngine() {
		InputStream originalIn = System.in;

		try {
			String simulatedInput = "3\n";
			System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

			GameEngine engine = GameEngine.createNewGame();
			assertNotNull(engine);
		} finally {
			System.setIn(originalIn);
		}
	}


	@ParameterizedTest
	@ValueSource(ints = {2, 3, 4, 5})
	public void createInitialDeck_withValidPlayerCount_createsCorrectDeck(int numPlayers) {
		CardFactory factory = new CardFactory();
		List<Card> deck = GameEngine.createInitialDeck(factory, numPlayers);

		assertNotNull(deck);
		assertFalse(deck.isEmpty());
		assertTrue(deck.stream().anyMatch(card
				-> card.getCardType() == CardType.ATTACK));
		assertTrue(deck.stream().anyMatch(card
				-> card.getCardType() == CardType.SKIP));
		assertTrue(deck.stream().anyMatch(card
				-> card.getCardType() == CardType.FAVOR));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   ", "\t", "\n"})
	public void processCommand_withNullOrEmptyInput_displaysError(String input) {
		gameEngine = createValidGameEngine();
		Player mockPlayer = createMockPlayer();

		mockUserInterface.displayError("Please enter a command. Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommands(input, mockPlayer);

		EasyMock.verify(mockUserInterface);
	}
}
