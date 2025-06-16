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

		gameEngine = createValidGameEngine();
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
	public void constructor_withNullPlayerManager_throwsNullPointerException() {
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
	public void constructor_withNullDeck_throwsNullPointerException() {
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
	public void constructor_withNullUI_allowsNullUI() {
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
	public void constructor_withNullCardFactory_throwsNullPointerException() {
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
	public void playCard_withNullPlayer_throwsNullPointerException() {
		SkipCard mockSkipCard = EasyMock.createMock(SkipCard.class);

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.playCard(null, mockSkipCard)
		);

		assertEquals("Player cannot be null", exception.getMessage());
	}

	@Test
	public void playCard_withNullCard_throwsNullPointerException() {
		Player mockPlayer = EasyMock.createMock(Player.class);

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.playCard(mockPlayer, null)
		);

		assertEquals("Card cannot be null", exception.getMessage());
	}

	@Test
	public void playCard_playerHasCard_executesCardEffect() {
		Hand hand = new Hand();
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
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> gameEngine.showAvailableCardTypes(null)
		);

		assertEquals("Player cannot be null", exception.getMessage());
	}

	@Test
	public void showAvailableCardTypes_withEmptyHand_printsNothing() {
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
		Player mockPlayer = EasyMock.createMock(Player.class);
		List<CardType> singleCardList = Arrays.asList(CardType.ATTACK);
		EasyMock.expect(mockPlayer.getAvailableCardTypes())
				.andReturn(singleCardList);
		EasyMock.replay(mockPlayer);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));
		;

		try {
			gameEngine.showAvailableCardTypes(mockPlayer);
			String expected = "Available cards: attack\n";
			String actual = normalizeOutputForAssertion(outputStream
					.toString(StandardCharsets.UTF_8));

			assertEquals(expected, actual);
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withMultipleCardTypes_printsCommaSeparatedList() {
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
			String expected = "Available cards: attack, skip, favor\n";
			String actual = normalizeOutputForAssertion(outputStream
					.toString(StandardCharsets.UTF_8));
			assertEquals(expected, actual);
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withUnderscoreCardType_replacesUnderscoresWithSpaces() {
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
			String expected = "Available cards: see the future, alter the future\n";
			String actual = normalizeOutputForAssertion(outputStream.
					toString(StandardCharsets.UTF_8));
			assertEquals(expected, actual);
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withMixedCardTypes_formatsAllCorrectly() {
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
			String expected = "Available cards: skip, see the future, normal, defuse\n";
			String actual = normalizeOutputForAssertion(outputStream.
					toString(StandardCharsets.UTF_8));
			assertEquals(expected, actual);
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withSingleCardTypeWithUnderscore_formatsCorrectly() {
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
			String expected = "Available cards: exploding kitten\n";
			String actual = normalizeOutputForAssertion(outputStream.
					toString(StandardCharsets.UTF_8));
			assertEquals(expected, actual);
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayer);
	}

	@Test
	public void showAvailableCardTypes_withAllCardTypes_printsCompleteList() {
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
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		assertThrows(NullPointerException.class,
				() -> gameEngine.handlePlayCommand(null, mockPlayer));
	}

	@Test
	public void handlePlayCommand_withNullPlayer_throwsNullPointerException() {
		String[] parts = {"play", "skip"};

		assertThrows(NullPointerException.class,
				() -> gameEngine.handlePlayCommand(parts, null));
	}

	@Test
	public void handlePlayCommand_withInsufficientParts_displaysUsageError() {
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

		EasyMock.verify(mockUserInterface, mockPlayer);
	}

	@Test
	public void handlePlayCommand_withEmptyParts_displaysUsageError() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.
				displayError("Usage: play <card_type>" +
						" (e.g., 'play skip' or 'play attack')");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		String[] parts = {};
		gameEngine.handlePlayCommand(parts, mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}

	@Test
	public void handlePlayCommand_withValidCardType_playsCard() {
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

		EasyMock.verify(mockPlayer, mockCardFactory, mockUserInterface,
				mockTurnManager, mockSkipCard, mockEffect);
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
		EasyMock.expect(mockFactory.createCards(CardType.REVERSE, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.REVERSE, FOUR_CARDS));
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
		final int TWELEVE_CARDS = 12;
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
		EasyMock.expect(mockFactory.createCards(CardType.REVERSE, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.REVERSE, TWO_CARDS));
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

		// currentCards = 2+2+2+2+3+2+1+2+2 = 18
		// targetNumberOfCards = 56 - 50 = 6
		// numberOfCardsNeeded = 18 - 6 = 12
		EasyMock.expect(mockFactory.createCards(CardType.NORMAL, TWELEVE_CARDS))
				.andReturn(createMockCardList(CardType.NORMAL, TWELEVE_CARDS));

		EasyMock.replay(mockFactory);

		List<Card> deck = GameEngine.createInitialDeck(mockFactory,
				FIFTY_PLAYERS);

		long normalCardCount = deck.stream()
				.filter(card -> card.getCardType() == CardType.NORMAL)
				.count();
		assertEquals(TWELEVE_CARDS, normalCardCount);

		EasyMock.verify(mockFactory);
	}

	@Test
	public void
	createInitialDeck_whenExactlyEnoughCards_doesNotAddNormalCards() {
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
		EasyMock.expect(mockFactory.createCards(CardType.REVERSE, FOUR_CARDS))
				.andReturn(createMockCardList(CardType.REVERSE, FOUR_CARDS));
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
		final int TWENTY_FOUR_PLAYERS = 24;

		List<Card> deck = GameEngine.createInitialDeck(mockFactory,
			TWENTY_FOUR_PLAYERS);

		long normalCardCount = deck.stream()
				.filter(card -> card.getCardType() == CardType.NORMAL)
				.count();
		assertEquals(0, normalCardCount);
		assertEquals(32, deck.size());

		// This will fail if createCards(NORMAL, 0) is called
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
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		EasyMock.expect(mockDeck.getDeckSize()).andReturn(0);
		EasyMock.replay(mockDeck);

		mockUserInterface.displayError("Deck is empty!");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.handleDrawCommand(mockPlayer);

		EasyMock.verify(mockPlayer, mockDeck, mockUserInterface);
	}


	@Test
	public void handleDrawCommand_withNormalCard_addsCardToHandAndEndsTurn() {
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

		EasyMock.verify(mockPlayer, mockDeck, mockUserInterface, mockTurnManager);
	}

	@Test
	public void handleDrawCommand_withExplodingKittenAndNoDefuse_removesPlayer() {
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

		EasyMock.verify(mockPlayer, mockDeck, mockUserInterface,
				mockTurnManager, mockPlayerManager);
	}

	@Test
	public void handleDrawCommand_withSkipCard_addsCardToHandAndEndsTurn() {
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

		EasyMock.verify(mockPlayer, mockDeck, mockUserInterface, mockTurnManager);
	}

	@Test
	public void handleDrawCommand_withDefuseCard_addsCardToHandAndEndsTurn() {
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

		EasyMock.verify(mockPlayer, mockDeck, mockUserInterface, mockTurnManager);
	}

	@Test
	public
	void
	handleDrawCommand_withExplodingKittenAndDefuse_usesDefuseAndReinsertsAndAdvancesTurn() {
		Card kitten = createMockCard(CardType.EXPLODING_KITTEN);
		final int DECK_SIZE = 5;
		final int EXPECTED_NUMBER_OF_CALLS = 2;
		Player player = EasyMock.createMock(Player.class);
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(DECK_SIZE)
				.times(EXPECTED_NUMBER_OF_CALLS);
		EasyMock.expect(mockDeck.draw()).andReturn(kitten).once();

		EasyMock.expect(player.hasCardType(CardType.DEFUSE))
				.andReturn(true).once();
		player.removeDefuseCard();
		EasyMock.expectLastCall().once();
		mockUserInterface.displayDrawnCard(kitten);
		EasyMock.expectLastCall().once();
		mockUserInterface.displayDefuseUsed();
		EasyMock.expectLastCall().once();
		String prompt =
				"Choose a position to insert the Exploding Kitten " +
						"(0 = bottom, 5 = top of deck)";
		final int MAX = 5;
		final int MIN = 0;

		EasyMock.expect(
				mockUserInterface.getNumericUserInput(prompt, MIN, MAX)
		).andReturn(EXPECTED_NUMBER_OF_CALLS).once();

		final int INDEX_TO_INSERT_CARD_AT = 2;
		mockDeck.insertCardAt(kitten, INDEX_TO_INSERT_CARD_AT);
		EasyMock.expectLastCall().once();
		mockUserInterface.displaySuccess(
				"Exploding Kitten placed back in the deck at position 2"
		);
		EasyMock.expectLastCall().once();

		mockTurnManager.advanceToNextPlayer();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockDeck, player, mockUserInterface, mockTurnManager);
		gameEngine.handleDrawCommand(player);
		EasyMock.verify(mockDeck, player, mockUserInterface, mockTurnManager);
	}

	@Test
	public void getPlayerChoiceForKittenPlacement_promptsWithDeckSizeAndReturnsChoice()
			{
		final int EXPECTED_RETURN_FOUR = 4;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(EXPECTED_RETURN_FOUR)
				.once();
		String prompt =
				"Choose a position to insert the Exploding Kitten " +
						"(0 = bottom, 4 = top of deck)";
		final int MIN = 0;
		final int MAX = 4;
		final int EXPECTED_RETURN_TWO = 2;
		EasyMock.expect(mockUserInterface.getNumericUserInput(prompt, MIN, MAX))
				.andReturn(EXPECTED_RETURN_TWO).once();

		EasyMock.replay(mockDeck, mockUserInterface);
		int choice = gameEngine.getPlayerChoiceForKittenPlacement();

		assertEquals(EXPECTED_RETURN_TWO, choice);

		EasyMock.verify(mockDeck, mockUserInterface);
	}


	@Test
	public void displayGameStatus_withTwoActivePlayers_displaysCorrectStatus() {
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

		EasyMock.verify(mockPlayerManager, mockDeck, mockTurnManager, mockCurrentPlayer);
	}



	@Test
	public void displayGameStatus_withOneActivePlayer_displaysCorrectStatus() {
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

		EasyMock.verify(mockPlayerManager, mockDeck, mockTurnManager, mockCurrentPlayer);
	}

	@Test
	public void displayGameStatus_withFiveActivePlayers_displaysCorrectStatus() {
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

		EasyMock.verify(mockPlayerManager, mockDeck, mockTurnManager, mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withTwoPlayersAndFullDeck_displaysCorrectState() {
		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockCurrentPlayer.isInGame()).andReturn(true);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer,
				mockActivePlayer());
		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(activePlayers);
		EasyMock.expect(mockPlayerManager.getPlayers())
				.andReturn(activePlayers).anyTimes();
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
			assertTrue(output.contains("Turn of player 0"));
			assertTrue(output.contains("Players remaining: 2"));
			assertTrue(output.contains("Active players indices: [0, 1]"));
			assertTrue(output.contains("Cards in deck: 20"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager, mockDeck, mockUserInterface, mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withOnePlayerRemaining_displaysCorrectState() {
		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockCurrentPlayer.isInGame()).andReturn(true);
		EasyMock.replay(mockCurrentPlayer);

		Player mockDefeatedPlayer = mockInactivePlayer();

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer);
		List<Player> allPlayers = Arrays.asList(mockCurrentPlayer, mockDefeatedPlayer);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.expect(mockPlayerManager.getPlayers())
				.andReturn(allPlayers).anyTimes();
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
			assertTrue(output.contains("Active players indices: [0]"));
			assertTrue(output.contains("Cards in deck: 5"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager, mockDeck, mockUserInterface, mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withFivePlayersRemaining_displaysCorrectState() {
		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockCurrentPlayer.isInGame()).andReturn(true);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(
				mockCurrentPlayer,
				mockActivePlayer(),
				mockActivePlayer(),
				mockActivePlayer(),
				mockActivePlayer()
		);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.expect(mockPlayerManager.getPlayers())
				.andReturn(activePlayers).anyTimes();
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

		EasyMock.verify(mockPlayerManager, mockDeck, mockUserInterface, mockCurrentPlayer);
	}

	@Test
	public void displayGameState_withEmptyDeck_displaysZeroCards() {
		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockCurrentPlayer.isInGame()).andReturn(true);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer);
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.expect(mockPlayerManager.getPlayers())
				.andReturn(activePlayers).anyTimes();
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

		EasyMock.verify(mockPlayerManager, mockDeck, mockUserInterface, mockCurrentPlayer);
	}

	@Test
	public void displayGameState_ensuresUserInterfaceDisplayPlayerHandCalled() {
		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockCurrentPlayer.isInGame()).andReturn(true);
		EasyMock.replay(mockCurrentPlayer);

		List<Player> activePlayers = Arrays.asList(mockCurrentPlayer);
		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(activePlayers);
		EasyMock.expect(mockPlayerManager.getPlayers())
				.andReturn(activePlayers).anyTimes();
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

		EasyMock.verify(mockPlayerManager, mockDeck, mockUserInterface, mockCurrentPlayer);
	}


	@Test
	public void displayGameState_withLargeNumbers_displaysCorrectly() {
		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockCurrentPlayer);
		List<Player> activePlayers = new ArrayList<>();
		final int NUMBER_OF_PLAYERS = 4;
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			activePlayers.add(EasyMock.createMock(Player.class));
		}
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.expect(mockPlayerManager.getPlayers())
				.andReturn(activePlayers).anyTimes();
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

		EasyMock.verify(mockPlayerManager, mockDeck, mockUserInterface, mockCurrentPlayer);
	}

	@Test
	public void handleQuitCommand_setsGameRunningToFalse() {
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
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.handleQuitCommand();
			String expected = "Thanks for playing Exploding Kittens!\n";
			String actual = normalizeOutputForAssertion(outputStream.
					toString(StandardCharsets.UTF_8));
			assertEquals(expected, actual);
		} finally {
			System.setOut(originalOut);
		}
	}

	@Test
	public void handlePlayerElimination_withTwoActivePlayers_syncsCorrectly() {
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

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_withOneActivePlayer_syncsCorrectly() {
		Player lastPlayer = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(lastPlayer);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(activePlayers);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_withThreeActivePlayers_syncsCorrectly() {
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

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_verifyMethodCallOrder() {
		Player player1 = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(player1);

		EasyMock.expect(mockPlayerManager.getActivePlayers()).andReturn(activePlayers);
		mockTurnManager.syncWith(activePlayers);
		EasyMock.expectLastCall();

		EasyMock.replay(mockPlayerManager);
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_passesExactListFromPlayerManager() {
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

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_withEmptyPlayerList_stillCallsSyncWith() {
		List<Player> emptyPlayerList = new ArrayList<>();

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(emptyPlayerList);
		EasyMock.replay(mockPlayerManager);

		mockTurnManager.syncWith(emptyPlayerList);
		EasyMock.expectLastCall();
		EasyMock.replay(mockTurnManager);

		gameEngine.handlePlayerGetsEliminated();

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_calledMultipleTimes_worksCorrectly() {
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

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void handlePlayerElimination_doesNotModifyPlayerList() {
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

		EasyMock.verify(mockPlayerManager, mockTurnManager);
	}

	@Test
	public void checkWinCondition_withTwoActivePlayers_gameStillRunning() {
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
	public void checkWinCondition_withOneActivePlayer_gameEndsWithWinner() {
		Player player1 = EasyMock.createMock(Player.class);
		List<Player> activePlayers = Arrays.asList(player1);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream, true,
				StandardCharsets.UTF_8));

		try {
			gameEngine.checkWinCondition();
			assertFalse(gameEngine.getIsGameRunning());
			String output = outputStream.toString(StandardCharsets.UTF_8);
			assertTrue(output.contains("GAME OVER! The last player " +
					"standing wins!"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
	}

	@Test
	public void checkWinCondition_withNoActivePlayers_gameEndsWithNoWinner() {
		List<Player> activePlayers = Arrays.asList();
		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream,
				true, StandardCharsets.UTF_8));

		try {
			gameEngine.checkWinCondition();
			assertFalse(gameEngine.getIsGameRunning());

			String output = outputStream.toString(StandardCharsets.UTF_8);
			assertTrue(output.contains("GAME OVER! Everyone exploded!"));
		} finally {
			System.setOut(originalOut);
		}

		EasyMock.verify(mockPlayerManager);
	}


	@Test
	public void processCommand_withNullInput_displaysError() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("Please enter a command. " +
				"Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand(null, mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}

	@Test
	public void processCommand_withEmptyInput_displaysError() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("" +
				"Please enter a command. Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("", mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}


	@Test
	public void processCommand_withWhitespaceOnlyInput_displaysError() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("Please enter " +
				"a command. Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("	  \t\n	", mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}


	@ParameterizedTest
	@MethodSource("provideCardTypeTestData")
	public void processCommand_withPlayCommandForDifferentCards_callsHandlePlayCommand(
			String cardName, CardType cardType, boolean endsTurn) {
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

		EasyMock.verify(mockPlayer, mockCardFactory, mockTurnManager,
				mockCard, mockEffect, mockUserInterface);
	}

	@Test
	public void
	processCommand_whenHandlePlayCommandThrowsException_displaysErrorMessage() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.parseCardType("invalidcard")).andThrow(
				new IllegalArgumentException("Invalid card type"));
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("Error executing command: Invalid card type");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("play invalidcard", mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}

	@Test
	public void processCommand_whenDrawThrowsException_catchesAndDisplaysError() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		final int NUMBER_OF_CARDS = 5;
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(NUMBER_OF_CARDS);
		EasyMock.expect(mockDeck.draw()).andThrow(new RuntimeException
				("Test exception"));
		mockUserInterface.displayError("Error executing command: " +
				"Test exception");
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer, mockDeck, mockUserInterface,
				mockTurnManager, mockPlayerManager);

		gameEngine.processCommand("draw", mockPlayer);
		EasyMock.verify(mockDeck, mockUserInterface, mockPlayer,
				mockTurnManager, mockPlayerManager);
	}

	@ParameterizedTest
	@ValueSource(strings = {"help"})
	public void processCommand_withHelpCommand_displaysHelp(String input) {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayHelp();
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand(input, mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}

	@ParameterizedTest
	@ValueSource(strings = {"hand"})
	public void processCommand_withHandCommand_displaysPlayerHand(String input) {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayPlayerHand(mockPlayer);
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand(input, mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}


	@Test
	public void processCommand_withQuitCommand_callsHandleQuitCommand() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("quit", mockPlayer);

		EasyMock.verify(mockPlayer);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"invalidcommand",
			"xyz",
			"notacommand",
			"playyy",
			"halp"
	})
	public void processCommand_withUnknownCommands_displaysUnknownCommandError
			(String input) {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.replay(mockPlayer);

		mockUserInterface.displayError("Unknown command: "
				+ input + ". Type 'help' for available commands.");
		EasyMock.expectLastCall();
		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand(input, mockPlayer);

		EasyMock.verify(mockUserInterface, mockPlayer);
	}

	@Test
	public void processCommand_withMultipleSpacesInPlayCommand_DoesntFail() {
		Player mockPlayer = EasyMock.createMock(Player.class);

		EasyMock.expect(mockPlayer.parseCardType("skip"))
				.andThrow(new IllegalArgumentException("Invalid card type"));

		mockUserInterface.displayError("Error executing command: Invalid card type");
		EasyMock.expectLastCall();
		EasyMock.replay(mockPlayer, mockUserInterface);
		gameEngine.processCommand("play	  skip", mockPlayer);

		EasyMock.verify(mockPlayer, mockUserInterface);
	}


	@Test
	public void processCommand_withStatusCommand_displaysGameStatus() {
		Player mockCurrentPlayer = EasyMock.createMock(Player.class);
		Player mockActivePlayer1 = EasyMock.createMock(Player.class);
		Player mockActivePlayer2 = EasyMock.createMock(Player.class);

		List<Player> activePlayers = Arrays.asList(mockActivePlayer1,
				mockActivePlayer2);

		EasyMock.expect(mockPlayerManager.getActivePlayers())
				.andReturn(activePlayers);
		EasyMock.replay(mockPlayerManager);

		final int EXPECTED_NUMBER_OF_CARDS = 10;
		EasyMock.expect(mockDeck.getDeckSize())
				.andReturn(EXPECTED_NUMBER_OF_CARDS);
		EasyMock.replay(mockDeck);

		EasyMock.expect(mockTurnManager.getCurrentActivePlayer())
				.andReturn(mockCurrentPlayer);
		EasyMock.replay(mockTurnManager);

		final int EXPECTED_NUMBER_OF_CARDS_TWO = 5;
		EasyMock.expect(mockCurrentPlayer.getNumberOfCards()).
				andReturn(EXPECTED_NUMBER_OF_CARDS_TWO);
		EasyMock.replay(mockCurrentPlayer);

		EasyMock.replay(mockUserInterface);

		gameEngine.processCommand("status", mockCurrentPlayer);

		EasyMock.verify(mockPlayerManager, mockDeck, mockTurnManager, mockCurrentPlayer);
	}

	@Test
	public void processCommand_withDrawCommand_callsHandleDrawCommand() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		Card mockCard = createMockCard(CardType.NORMAL);

		EasyMock.expect(mockDeck.getDeckSize()).andReturn(1);
		EasyMock.expect(mockDeck.draw()).andReturn(mockCard);

		mockPlayer.addCardToHand(mockCard);
		EasyMock.expectLastCall();

		mockUserInterface.displayDrawnCard(mockCard);
		EasyMock.expectLastCall();
		mockTurnManager.advanceToNextPlayer();
		EasyMock.expectLastCall();

		EasyMock.replay(
				mockTurnManager,
				mockPlayerManager,
				mockDeck,
				mockPlayer,
				mockUserInterface,
				mockCardFactory,
				mockSecureRandom
		);

		gameEngine.processCommand("draw", mockPlayer);
		EasyMock.verify(
				mockTurnManager,
				mockPlayerManager,
				mockDeck,
				mockPlayer,
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

	private String normalizeOutputForAssertion(String rawOutput) {
		return rawOutput
				.replace("\r\n", "\n")
				.replace("\r", "\n");
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

	private static Stream<Arguments> provideCardTypeTestData() {
		return Stream.of(
				Arguments.of("skip", CardType.SKIP, true),
				Arguments.of("attack", CardType.ATTACK, true),
				Arguments.of("favor", CardType.FAVOR, false),
				Arguments.of("shuffle", CardType.SHUFFLE, false)
		);
	}

	private Player mockActivePlayer() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.isInGame()).andReturn(true).anyTimes();
		EasyMock.replay(mockPlayer);
		return mockPlayer;
	}

	private Player mockInactivePlayer() {
		Player mockPlayer = EasyMock.createMock(Player.class);
		EasyMock.expect(mockPlayer.isInGame()).andReturn(false).anyTimes();
		EasyMock.replay(mockPlayer);
		return mockPlayer;
	}
}
