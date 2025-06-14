package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ui.UserInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
	private GameEngine gameEngine;
	private TurnManager mockTurnManager;
	private PlayerManager mockPlayerManager;
	private UserInterface mockUserInterface;
	private Deck mockDeck;
	private CardFactory mockCardFactory;
	private SecureRandom mockSecureRandom;
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 5;
	private static final int THREE_PLAYERS = 3;
	private static final int FOUR_PLAYERS = 4;

	@BeforeEach
	public void setUp() {
		mockTurnManager = EasyMock.createMock(TurnManager.class);
		mockPlayerManager = EasyMock.createMock(PlayerManager.class);
		mockUserInterface = EasyMock.createMock(UserInterface.class);
		mockDeck = EasyMock.createMock(Deck.class);
		mockCardFactory = EasyMock.createMock(CardFactory.class);
		mockSecureRandom = EasyMock.createMock(SecureRandom.class);

		gameEngine = new GameEngine(
				mockTurnManager,
				mockPlayerManager,
				mockDeck,
				mockUserInterface,
				mockCardFactory,
				mockSecureRandom
		);
	}

	private GameEngine createValidGameEngine() {
		return new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);
	}

	private Player createMockPlayer() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		final int NUMBER_OF_CARDS = 5;
		EasyMock.expect(mockPlayer
				.getNumberOfCards())
				.andStubReturn(NUMBER_OF_CARDS);
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

	@Test
	public void constructor_withNullTurnManager_throwsNullPointerException() {
		NullPointerException thrown = assertThrows(
				NullPointerException.class,
				() -> new GameEngine(
						null,
						mockPlayerManager,
						mockDeck,
						mockUserInterface,
						mockCardFactory,
						mockSecureRandom
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
						mockCardFactory,
						mockSecureRandom
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
						mockCardFactory,
						mockSecureRandom
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
				mockCardFactory,
				mockSecureRandom
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
						null,
						mockSecureRandom));
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
			System.setIn(new ByteArrayInputStream(simulatedInput
					.getBytes(StandardCharsets.UTF_8)));

			GameEngine engine = GameEngine.createNewGame();
			assertNotNull(engine);
		} finally {
			System.setIn(originalIn);
		}
	}


	@ParameterizedTest
	@ValueSource(ints = {MIN_PLAYERS, THREE_PLAYERS, FOUR_PLAYERS, MAX_PLAYERS})
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
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals("", outputStream.
					toString(StandardCharsets.UTF_8));
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
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));;

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals("Available cards: attack\n",
					outputStream.toString(StandardCharsets.UTF_8));
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
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals("Available cards: attack, skip, favor\n",
					outputStream.toString(StandardCharsets.UTF_8));
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
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals(
					"Available cards: see the future, " +
							"alter the future\n",
					outputStream.toString(StandardCharsets.UTF_8));
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
				CardType.SKIP,
				CardType.SEE_THE_FUTURE,
				CardType.NORMAL,
				CardType.DEFUSE);
		EasyMock.expect(mockPlayer.getAvailableCardTypes()).andReturn(mixedCardsList);
		EasyMock.replay(mockPlayer);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals(
					"Available cards: skip, " +
							"see the future, normal, defuse\n",
					outputStream.toString(StandardCharsets.UTF_8));
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
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			assertEquals(
					"Available cards:" +
							" exploding kitten\n",
					outputStream.toString(StandardCharsets.UTF_8));
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
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);
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

	@Test
	public void handlePlayCommand_withInsufficientParts_displaysUsageError() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface
				.displayError(
						"Usage: play <card_type> " +
								"(e.g., 'play skip'" +
								" or 'play attack')");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		String[] parts = {"play"};
		gameEngine.handlePlayCommand(parts, mockPlayer);

		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockPlayer);
	}

	@Test
	public void handlePlayCommand_withEmptyParts_displaysUsageError() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.
				displayError("Usage: play <card_type>" +
						" (e.g., 'play skip' or 'play attack')");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		String[] parts = {};
		gameEngine.handlePlayCommand(parts, mockPlayer);

		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockPlayer);
	}

	@Test
	public void handlePlayCommand_withValidCardType_playsCard() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Card mockSkipCard = createMockCard(CardType.SKIP);
		EasyMock.reset(mockSkipCard);
		CardEffect mockEffect = EasyMock.createMock(CardEffect.class);
		mockEffect.execute(EasyMock.anyObject(GameContext.class));
		EasyMock.expectLastCall().andAnswer(() -> {
			GameContext context = (GameContext)
					EasyMock.getCurrentArguments()[0];
			context.endTurnWithoutDrawing();
			return null;
		});
		EasyMock.replay(mockEffect);
		EasyMock.expect(mockSkipCard.createEffect()).andReturn(mockEffect);
		EasyMock.replay(mockSkipCard);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.parseCardType("skip")).andReturn(CardType.SKIP);
		mockPlayer.removeCardFromHand(mockSkipCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer);

		EasyMock.expect(mockCardFactory
				.createCard(CardType.SKIP)).andReturn(mockSkipCard);
		EasyMock.replay(mockCardFactory);

		mockUserInterface.displayCardPlayed(mockSkipCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		mockTurnManager.endTurnWithoutDraw();
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		String[] parts = {"play", "skip"};
		gameEngine.handlePlayCommand(parts, mockPlayer);

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockCardFactory);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockTurnManager);
		EasyMock.verify(mockSkipCard);
		EasyMock.verify(mockEffect);
	}

	private List<Card> createMockCardList(CardType cardType, int count) {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Card mockCard = EasyMock.createMock(Card.class);
			EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
			EasyMock.replay(mockCard);
			cards.add(mockCard);
		}
		return cards;
	}

	@Test
	public void createInitialDeck_whenNoNormalCardsNeeded_doesNotAddNormalCards() {
		CardFactory mockFactory = EasyMock.createMock(CardFactory.class);

		final int ONE_CARD = 1;
		final int TWO_CARDS = 2;

		final int FOUR_CARDS = 4;
		final int FIVE_CARDS = 5;
		EasyMock.expect(mockFactory.createCards(CardType.ATTACK, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.ATTACK, FOUR_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.SKIP, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.SKIP, FOUR_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.FAVOR, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.FAVOR, FOUR_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.SHUFFLE, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.SHUFFLE, FOUR_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.SEE_THE_FUTURE,
						FIVE_CARDS))
				.andReturn(createMockCardList(CardType.SEE_THE_FUTURE,
						FIVE_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.ALTER_THE_FUTURE,
						FOUR_CARDS))
				.andReturn(createMockCardList(CardType.ALTER_THE_FUTURE,
						FOUR_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.NUKE, ONE_CARD))
				.andReturn(createMockCardList(CardType.NUKE, ONE_CARD));
		EasyMock.expect(mockFactory.createCards(CardType.DEFUSE, TWO_CARDS))
				.andReturn(createMockCardList(CardType.DEFUSE, TWO_CARDS));

		EasyMock.replay(mockFactory);

		final int TWO_PLAYERS = 2;
		List<Card> deck = GameEngine.createInitialDeck(mockFactory, TWO_PLAYERS);

		long normalCardCount = deck.stream()
				.filter(card -> card.getCardType() == CardType.NORMAL)
				.count();
		assertEquals(0, normalCardCount);

		EasyMock.verify(mockFactory);
	}

	@Test
	public void createInitialDeck_whenNormalCardsNeeded_addsCorrectNumberOfNormalCards() {
		CardFactory mockFactory = EasyMock.createMock(CardFactory.class);

		final int ONE_CARD = 1;
		final int TWO_CARDS = 2;
		final int THREE_CARDS = 3;
		final int FOUR_CARDS = 4;
		final int FIVE_CARDS = 5;
		final int TEN_CARDS = 10;
		// user interface wouldn't let this go through
		// but this is for some of the mutation test
		final int FIFTY_PLAYERS = 50;
		EasyMock.expect(mockFactory.createCards(CardType.ATTACK, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.ATTACK, TWO_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.SKIP, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.SKIP, TWO_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.FAVOR, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.FAVOR, TWO_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.SHUFFLE, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.SHUFFLE, TWO_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.SEE_THE_FUTURE,
						FIVE_CARDS))
				.andReturn(createMockCardList(CardType.
						SEE_THE_FUTURE, THREE_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.ALTER_THE_FUTURE,
						FOUR_CARDS))
				.andReturn(createMockCardList(CardType.
						ALTER_THE_FUTURE, TWO_CARDS));
		EasyMock.expect(mockFactory.createCards(CardType.NUKE, ONE_CARD))
				.andReturn(createMockCardList(CardType.NUKE, ONE_CARD));
		EasyMock.expect(mockFactory.createCards(CardType.DEFUSE, TWO_CARDS))
				.andReturn(createMockCardList(CardType.DEFUSE, TWO_CARDS));

		// currentCards = 2+2+2+2+3+2+1+2 = 16
		// targetNumberOfCards = 56 - 50 = 6
		// numberOfCardsNeeded = 16 - 6 = 10
		EasyMock.expect(mockFactory.createCards(CardType.NORMAL, TEN_CARDS))
				.andReturn(createMockCardList(CardType.NORMAL, TEN_CARDS));

		EasyMock.replay(mockFactory);

		List<Card> deck = GameEngine.createInitialDeck(mockFactory,
				FIFTY_PLAYERS);

		long normalCardCount = deck.stream()
				.filter(card -> card.getCardType() == CardType.NORMAL)
				.count();
		assertEquals(TEN_CARDS, normalCardCount);

		EasyMock.verify(mockFactory);
	}

	@Test
	public void handleDrawCommand_withNullPlayer_throwsNullPointerException() {
		gameEngine = createValidGameEngine();

		assertThrows(NullPointerException.class,
				() -> gameEngine.handleDrawCommand(null));
	}

	@Test
	public void handleDrawCommand_withEmptyDeck_displaysErrorAndReturns() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		EasyMock.expect(mockDeck.getDeckSize()).andReturn(0);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayError("Deck is empty!");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.handleDrawCommand(mockPlayer);

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
	}


	@Test
	public void handleDrawCommand_withNormalCard_addsCardToHandAndEndsTurn() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Card mockNormalCard = createMockCard(CardType.NORMAL);

		Player mockPlayer = EasyMock.createMock(Player.class);
		mockPlayer.addCardToHand(mockNormalCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer);

		final int NUMBER_OF_CARDS = 10;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS
		);
		EasyMock.expect(mockDeck.draw()).andReturn(mockNormalCard);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayDrawnCard(mockNormalCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		mockTurnManager.advanceToNextPlayer();
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handleDrawCommand(mockPlayer);

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handleDrawCommand_withExplodingKitten_usesDefuseAndEndsTurn() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Card mockExplodingKitten = createMockCard(CardType.EXPLODING_KITTEN);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.hasCardType(CardType.DEFUSE))
				.andReturn(true);
		mockPlayer.removeDefuseCard();
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer);

		final int NUMBER_OF_CARDS = 10;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(NUMBER_OF_CARDS
		).times(2);
		EasyMock.expect(mockDeck.draw()).andReturn(mockExplodingKitten);
		mockDeck.insertCardAt(EasyMock.eq(mockExplodingKitten),
				EasyMock.anyInt());
		EasyMock.expectLastCall();
		EasyMock.replay(mockDeck);

		mockUserInterface.displayDrawnCard(mockExplodingKitten);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		mockTurnManager.advanceToNextPlayer();
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.handleDrawCommand(mockPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);
			assertTrue
					(output.contains("You drew an Exploding Kitten" +
							" but used a Defuse card!"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handleDrawCommand_withExplodingKittenAndNoDefuse_removesPlayer() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Card mockExplodingKitten = createMockCard(CardType.EXPLODING_KITTEN);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.hasCardType(CardType.DEFUSE))
				.andReturn(false);
		EasyMock.replay(mockPlayer);

		final int DECK_SIZE = 10;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(DECK_SIZE);
		EasyMock.expect(mockDeck.draw()).andReturn(mockExplodingKitten);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayDrawnCard(mockExplodingKitten);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		mockPlayerManager.removePlayerFromGame(mockPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayerManager);

		EasyMock.replay(mockTurnManager);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.handleDrawCommand(mockPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);
			assertTrue(output.contains
					("BOOM! You drew an Exploding Kitten and had" +
							" no Defuse card!"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handleDrawCommand_withSkipCard_addsCardToHandAndEndsTurn() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Card mockSkipCard = createMockCard(CardType.SKIP);

		Player mockPlayer = EasyMock.createMock(Player.class);
		mockPlayer.addCardToHand(mockSkipCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer);
		final int DECK_SIZE = 5;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(DECK_SIZE);
		EasyMock.expect(mockDeck.draw()).andReturn(mockSkipCard);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayDrawnCard(mockSkipCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		mockTurnManager.advanceToNextPlayer();
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handleDrawCommand(mockPlayer);

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handleDrawCommand_withDefuseCard_addsCardToHandAndEndsTurn() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Card mockDefuseCard = createMockCard(CardType.DEFUSE);

		Player mockPlayer = EasyMock.createMock(Player.class);
		mockPlayer.addCardToHand(mockDefuseCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer);

		final int DECK_SIZE = 8;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(DECK_SIZE);
		EasyMock.expect(mockDeck.draw()).andReturn(mockDefuseCard);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayDrawnCard(mockDefuseCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		mockTurnManager.advanceToNextPlayer();
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handleDrawCommand(mockPlayer);

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void displayGameStatus_withTwoActivePlayers_displaysCorrectStatus() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		final int NUMBER_OF_CARDS = 5;
		EasyMock.expect(mockCurrentPlayer
				.getNumberOfCards()).andReturn(NUMBER_OF_CARDS);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer,
				EasyMock.createMock(Player.class));
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int DECK_SIZE = 15;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(DECK_SIZE);
		EasyMock.replay(mockDeck);

		EasyMock.expect(mockTurnManager.getCurrentActivePlayer())
				.andReturn(mockCurrentPlayer);
		EasyMock.replay(mockTurnManager);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream
				, true, StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameStatus();
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(output.contains("=== GAME STATUS ==="));
			assertTrue(output.contains("Active players: 2"));
			assertTrue(output.contains("Cards in deck: 15"));
			assertTrue(output.contains("Current player has 5 cards"));
			assertTrue(output.contains("=================="));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockTurnManager);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void displayGameStatus_withOneActivePlayer_displaysCorrectStatus() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager,
				mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		final int NUMBER_OF_CARDS_FOR_MOCK_PLAYER = 3;
		EasyMock.expect(mockCurrentPlayer.getNumberOfCards())
				.andReturn(NUMBER_OF_CARDS_FOR_MOCK_PLAYER);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 8;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS_IN_DECK
		);
		EasyMock.replay(mockDeck);

		EasyMock.expect(mockTurnManager.getCurrentActivePlayer())
				.andReturn(mockCurrentPlayer);
		EasyMock.replay(mockTurnManager);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream
				, true, StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameStatus();
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(output.contains("Active players: 1"));
			assertTrue(output.contains("Cards in deck: 8"));
			assertTrue(output.contains("Current player has 3 cards"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockTurnManager);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void displayGameStatus_withFiveActivePlayers_displaysCorrectStatus() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		final int NUMBER_OF_CARDS_IN_PLAYERS_HAND = 7;
		EasyMock.expect(mockCurrentPlayer
				.getNumberOfCards()).andReturn(NUMBER_OF_CARDS_IN_PLAYERS_HAND);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(
				mockCurrentPlayer,
				EasyMock.createMock(Player.class),
				EasyMock.createMock(Player.class),
				EasyMock.createMock(Player.class),
				EasyMock.createMock(Player.class)
		);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 25;
		EasyMock.expect(mockDeck.getDeckSize())
				.andReturn(NUMBER_OF_CARDS_IN_DECK);
		EasyMock.replay(mockDeck);

		EasyMock.expect(mockTurnManager.getCurrentActivePlayer()
		).andReturn(mockCurrentPlayer);
		EasyMock.replay(mockTurnManager);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameStatus();
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(output.contains("Active players: 5"));
			assertTrue(output.contains("Cards in deck: 25"));
			assertTrue(output.contains("Current player has 7 cards"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockTurnManager);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withTwoPlayersAndFullDeck_displaysCorrectState() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer,
				EasyMock.createMock(Player.class));
		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 20;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS_IN_DECK
		);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayPlayerHand(mockCurrentPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameState(mockCurrentPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(
					output.contains
							("=========================" +
									"==============="));
			assertTrue(output.contains("Current Player's Turn"));
			assertTrue(output.contains("Players remaining: 2"));
			assertTrue(output.contains("Cards in deck: 20"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withOnePlayerRemaining_displaysCorrectState() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 5;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS_IN_DECK
		);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayPlayerHand(mockCurrentPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameState(mockCurrentPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(output.contains("Players remaining: 1"));
			assertTrue(output.contains("Cards in deck: 5"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withFivePlayersRemaining_displaysCorrectState() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(
				mockCurrentPlayer,
				EasyMock.createMock(Player.class),
				EasyMock.createMock(Player.class),
				EasyMock.createMock(Player.class),
				EasyMock.createMock(Player.class)
		);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 35;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS_IN_DECK
		);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayPlayerHand(mockCurrentPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameState(mockCurrentPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(output.contains("Players remaining: 5"));
			assertTrue(output.contains("Cards in deck: 35"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withEmptyDeck_displaysZeroCards() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 0;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS_IN_DECK
		);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayPlayerHand(mockCurrentPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameState(mockCurrentPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(output.contains("Cards in deck: 0"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void displayGameState_ensuresUserInterfaceDisplayPlayerHandCalled() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer);
		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 8;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS_IN_DECK
		);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayPlayerHand(mockCurrentPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameState(mockCurrentPlayer);
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockCurrentPlayer);
	}


	@Test
	public void displayGameState_withLargeNumbers_displaysCorrectly() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockCurrentPlayer);
		List<Player> activePlayers = new ArrayList<>();
		final int NUMBER_OF_PLAYERS = 4;
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			activePlayers.add(EasyMock.createMock(Player.class));
		}
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int NUMBER_OF_CARDS_IN_DECK = 54;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(
				NUMBER_OF_CARDS_IN_DECK
		);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayPlayerHand(mockCurrentPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.displayGameState(mockCurrentPlayer);
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertTrue(output.contains("Players remaining: 4"));
			assertTrue(output.contains("Cards in deck: 54"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockDeck);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockCurrentPlayer);
	}

	@Test
	public void handleQuitCommand_setsGameRunningToFalse() {
		gameEngine = createValidGameEngine();
		assertTrue(gameEngine.getIsGameRunning());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.handleQuitCommand();
			assertFalse(gameEngine.getIsGameRunning());
		} finally {
			System.setOut(originalOut);
		}
	}

	@Test
	public void handleQuitCommand_displaysThankYouMessage() {
		gameEngine = createValidGameEngine();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.handleQuitCommand();
			String output = outputStream.toString(StandardCharsets.UTF_8);

			assertEquals(
					"Thanks for playing Exploding Kittens!\n", output);
		} finally {
			System.setOut(originalOut);
		}
	}

	@Test
	public void handlePlayerElimination_withTwoActivePlayers_syncsCorrectly() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(player1, player2);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(activePlayers);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_withOneActivePlayer_syncsCorrectly() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player lastPlayer = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(lastPlayer);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(activePlayers);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_withThreeActivePlayers_syncsCorrectly() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);
		Player player3 = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(player1, player2, player3);

		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(activePlayers);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_verifyMethodCallOrder() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(player1);

		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(activePlayers);
		mockTurnManager.syncWith(activePlayers);
		EasyMock.expectLastCall();

		EasyMock.replay(mockPlayerManager);
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_passesExactListFromPlayerManager() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);

		List<Player> specificPlayerList = new ArrayList<>();
		specificPlayerList.add(player1);
		specificPlayerList.add(player2);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(specificPlayerList);
		EasyMock.replay(mockPlayerManager);
		mockTurnManager.syncWith(EasyMock.same(specificPlayerList));
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_withEmptyPlayerList_stillCallsSyncWith() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		List<Player> emptyPlayerList = new ArrayList<>();

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(emptyPlayerList);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(emptyPlayerList);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_calledMultipleTimes_worksCorrectly() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);
		List<Player> twoPlayers = Arrays.asList(player1, player2);

		List<Player> onePlayer = Arrays.asList(player1);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(twoPlayers)
				.andReturn(onePlayer);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(twoPlayers);
		EasyMock.expectLastCall();
		mockTurnManager.syncWith(onePlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();
		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_doesNotModifyPlayerList() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);

		List<Player> originalList = new ArrayList<>();
		originalList.add(player1);
		originalList.add(player2);
		int originalSize = originalList.size();

		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(originalList);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(originalList);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();
		assertEquals(originalSize, originalList.size());
		assertTrue(originalList.contains(player1));
		assertTrue(originalList.contains(player2));

		EasyMock.verify(mockPlayerManager);
		EasyMock.verify(mockTurnManager);
	}

	@Test
	public void checkWinCondition_withTwoActivePlayers_gameStillRunning() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager,
				mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(player1, player2);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.checkWinCondition();
			assertTrue(gameEngine.getIsGameRunning());
			assertEquals("",
					outputStream.toString(StandardCharsets.UTF_8));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
	}

	@Test
	public void checkWinCondition_withThreeActivePlayers_gameStillRunning() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager,
				mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player player1 = EasyMock.createMock(Player.class);
		Player player2 = EasyMock.createMock(Player.class);
		Player player3 = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(player1, player2, player3);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.checkWinCondition();

			assertTrue(gameEngine.getIsGameRunning());
			assertEquals("", outputStream
					.toString(StandardCharsets.UTF_8));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
	}

	@Test
	public void checkWinCondition_withFiveActivePlayers_gameStillRunning() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager,
				mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		List<Player> activePlayers = new ArrayList<>();
		final int NUMBER_OF_PLAYERS = 5;
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			activePlayers.add(EasyMock.createMock(Player.class));
		}

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.checkWinCondition();
			assertTrue(gameEngine.getIsGameRunning());
			assertEquals("", outputStream
					.toString(StandardCharsets.UTF_8));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
	}

	@Test
	public void processCommand_withNullInput_displaysError() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("Please enter a command. " +
				"Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand(null, mockPlayer);

		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockPlayer);
	}

	@Test
	public void processCommand_withEmptyInput_displaysError() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("" +
				"Please enter a command. Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("", mockPlayer);

		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockPlayer);
	}


	@Test
	public void processCommand_withWhitespaceOnlyInput_displaysError() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("Please enter " +
				"a command. Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("   \t\n  ", mockPlayer);

		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockPlayer);
	}

	private static Stream<Arguments> provideCardTypeTestData() {
		return Stream.of(
				Arguments.of("skip", CardType.SKIP, true),
				Arguments.of("attack", CardType.ATTACK, true),
				Arguments.of("favor", CardType.FAVOR, false),
				Arguments.of("shuffle", CardType.SHUFFLE, false)
		);
	}

	@ParameterizedTest
	@MethodSource("provideCardTypeTestData")
	public
	void processCommand_withPlayCommandForDifferentCards_callsHandlePlayCommand(
			String cardName, CardType cardType, boolean endsTurn) {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Card mockCard = createMockCard(cardType);
		EasyMock.reset(mockCard);
		CardEffect mockEffect = EasyMock.createMock(CardEffect.class);
		mockEffect.execute(EasyMock.anyObject(GameContext.class));
		EasyMock.expectLastCall().andAnswer(() -> {
			GameContext context = (GameContext) EasyMock
					.getCurrentArguments()[0];
			if (endsTurn) {
				context.endTurnWithoutDrawing();
			}
			return null;
		});
		EasyMock.replay(mockEffect);
		EasyMock.expect(mockCard.createEffect()).andReturn(mockEffect);
		EasyMock.replay(mockCard);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.parseCardType(cardName))
				.andReturn(cardType);
		mockPlayer.removeCardFromHand(mockCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer);

		EasyMock.expect(mockCardFactory.createCard(cardType))
				.andReturn(mockCard);
		EasyMock.replay(mockCardFactory);

		mockUserInterface.displayCardPlayed(mockCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		if (endsTurn) {
			mockTurnManager.endTurnWithoutDraw();
			EasyMock.expectLastCall();
		}
		EasyMock.replay(mockTurnManager);

		gameEngine.processCommand("play " + cardName, mockPlayer);

		EasyMock.verify(mockPlayer);
		EasyMock.verify(mockCardFactory);
		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockTurnManager);
		EasyMock.verify(mockCard);
		EasyMock.verify(mockEffect);
	}

	@Test
	public
	void
	processCommand_whenHandlePlayCommandThrowsException_displaysErrorMessage() {
		gameEngine = new GameEngine(mockTurnManager, mockPlayerManager, mockDeck,
				mockUserInterface, mockCardFactory, mockSecureRandom);

		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.parseCardType("invalidcard")).andThrow(
				new IllegalArgumentException("Invalid card type"));
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("Error executing command: Invalid card type");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("play invalidcard", mockPlayer);

		EasyMock.verify(mockUserInterface);
		EasyMock.verify(mockPlayer);
	}
}
