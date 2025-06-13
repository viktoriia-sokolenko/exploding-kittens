package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ui.UserInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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

//	private Player createMockPlayerWithMultipleCards() {
//		Player mockPlayer = EasyMock.createMock(Player.class);
//
//		// Create a mock SkipCard to return when asked for card at index 0
//		Card mockSkipCard = createMockCard(CardType.SKIP);
//
//		// Mock the methods that would be called by getCardFromHandByIndex
//		EasyMock.expect(mockPlayer.get)).andReturn(mockSkipCard);
//		EasyMock.expect(mockPlayer.getNumberOfCards()).andStubReturn(5);
//		EasyMock.expect(mockPlayer.isInGame()).andStubReturn(true);
//
//		EasyMock.replay(mockPlayer);
//		return mockPlayer;
//	}

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

	private Player createMockPlayerWithCards() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.getCardTypeCount(EasyMock.anyObject())).andStubReturn(1);
		EasyMock.expect(mockPlayer.getNumberOfCards()).andStubReturn(5);
		EasyMock.expect(mockPlayer.isInGame()).andStubReturn(true);
		EasyMock.replay(mockPlayer);
		return mockPlayer;
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

//	@ParameterizedTest
//	@NullAndEmptySource
//	@ValueSource(strings = {"   ", "\t", "\n"})
//	public void processCommand_withNullOrEmptyInput_displaysError(String input) {
//		gameEngine = createValidGameEngine();
//		Player mockPlayer = createMockPlayer();
//
//		mockUserInterface.displayError("Please enter a command. Type 'help' for available commands.");
//		EasyMock.expectLastCall();
//		EasyMock.replay(mockUserInterface);
//
//		gameEngine.processCommand(input, mockPlayer);
//
//		EasyMock.verify(mockUserInterface);
//	}

	@Test
	public void showAvailableCardTypes_withNullPlayer_throwsNullPointerException() {
		gameEngine = createValidGameEngine();

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.showAvailableCardTypes(null)
		);

		assertEquals("Player cannot be null", exception.getMessage());
	}

	@Test
	public void showAvailableCardTypes_withEmptyHand_printsNothing() {
		gameEngine = createValidGameEngine();
		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> emptyList = new ArrayList<>();
		EasyMock.expect(mockPlayer.getAvailableCardTypes())
				.andReturn(emptyList);
		EasyMock.replay(mockPlayer);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals("", outputStream.toString());
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withOneCardType_printsFormattedCardType() {
		gameEngine = createValidGameEngine();

		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> singleCardList = Arrays.asList(CardType.ATTACK);
		EasyMock.expect(mockPlayer.getAvailableCardTypes())
				.andReturn(singleCardList);
		EasyMock.replay(mockPlayer);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals("Available cards: attack\n",
					outputStream.toString());
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withMultipleCardTypes_printsCommaSeparatedList() {
		gameEngine = createValidGameEngine();

		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> multipleCardsList = Arrays.asList(
				CardType.ATTACK, CardType.SKIP, CardType.FAVOR);
		EasyMock.expect(mockPlayer.getAvailableCardTypes())
				.andReturn(multipleCardsList);
		EasyMock.replay(mockPlayer);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals("Available cards: attack, skip, favor\n",
					outputStream.toString());
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withUnderscoreCardType_replacesUnderscoresWithSpaces() {
		gameEngine = createValidGameEngine();
		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> underscoreCardsList = Arrays.asList(
				CardType.SEE_THE_FUTURE, CardType.ALTER_THE_FUTURE);
		EasyMock.expect(mockPlayer.getAvailableCardTypes())
				.andReturn(underscoreCardsList);
		EasyMock.replay(mockPlayer);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals(
					"Available cards: see the future, " +
							"alter the future\n",
					outputStream.toString());
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withMixedCardTypes_formatsAllCorrectly() {
		gameEngine = createValidGameEngine();
		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> mixedCardsList = Arrays.asList(
				CardType.SKIP, CardType.SEE_THE_FUTURE, CardType.NORMAL, CardType.DEFUSE);
		EasyMock.expect(mockPlayer.getAvailableCardTypes()).andReturn(mixedCardsList);
		EasyMock.replay(mockPlayer);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals(
					"Available cards: skip, " +
							"see the future, normal, defuse\n",
					outputStream.toString());
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withSingleCardTypeWithUnderscore_formatsCorrectly() {
		gameEngine = createValidGameEngine();

		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> singleUnderscoreCard =
				List.of(CardType.EXPLODING_KITTEN);
		EasyMock.expect(mockPlayer.getAvailableCardTypes()
		).andReturn(singleUnderscoreCard);
		EasyMock.replay(mockPlayer);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals(
					"Available cards:" +
							" exploding kitten\n",
					outputStream.toString());
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withAllCardTypes_printsCompleteList() {
		gameEngine = createValidGameEngine();

		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> allCardTypes = Arrays.asList(CardType.values());
		EasyMock.expect(mockPlayer.getAvailableCardTypes()).andReturn(allCardTypes);
		EasyMock.replay(mockPlayer);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			String output = outputStream.toString();
			assertTrue(output.startsWith("Available cards: "));
			assertTrue(output.endsWith("\n"));
			assertTrue(output.contains("see the future"));
			assertTrue(output.contains("alter the future"));
			assertTrue(output.contains("exploding kitten"));
			assertTrue(output.contains(", "));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void handlePlayCommand_withNullParts_throwsNullPointerException() {
		gameEngine = createValidGameEngine();
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		assertThrows(NullPointerException.class,
				() -> gameEngine.handlePlayCommand(null, mockPlayer));
	}

	@Test
	public void handlePlayCommand_withNullPlayer_throwsNullPointerException() {
		gameEngine = createValidGameEngine();
		String[] parts = {"play", "skip"};

		assertThrows(NullPointerException.class,
				() -> gameEngine.handlePlayCommand(parts, null));
	}

}
