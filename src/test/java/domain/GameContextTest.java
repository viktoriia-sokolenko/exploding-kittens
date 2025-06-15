package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ui.UserInterface;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameContextTest {
	private GameContext gameContext;
	private Player mockCurrentPlayer;
	private TurnManager mockTurnManager;
	private PlayerManager mockPlayerManager;
	private Deck mockDeck;
	private UserInterface userInterface;
	private CardFactory mockCardFactory;
	private static final int DEFAULT_PLAYERS = 3;


	@BeforeEach
	public void setUp() {
		mockCurrentPlayer = EasyMock.createMock(Player.class);
		gameContext = new GameContext(mockCurrentPlayer);
		mockTurnManager = EasyMock.createMock(TurnManager.class);
		mockDeck = EasyMock.createMock(Deck.class);
		mockPlayerManager = EasyMock.createMock(PlayerManager.class);
		userInterface = EasyMock.createMock(UserInterface.class);
		mockCardFactory = EasyMock.createMock(CardFactory.class);
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
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		assertNotNull(fullGameContext);
		assertEquals(mockCurrentPlayer, fullGameContext.getCurrentPlayer());
	}

	@Test
	void constructor_withNullTurnManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(null, mockPlayerManager,
					mockDeck, mockCurrentPlayer,
					userInterface, mockCardFactory);
		});
	}

	@Test
	void constructor_withNullPlayerManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, null, mockDeck,
					mockCurrentPlayer, userInterface, mockCardFactory);
		});
	}

	@Test
	void constructor_withNullDeck_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, mockPlayerManager, null,
					mockCurrentPlayer, userInterface, mockCardFactory);
		});
	}

	@Test
	void constructor_withNullCurrentPlayer_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, mockPlayerManager, mockDeck,
					null, userInterface, mockCardFactory);
		});
	}

	@Test
	void fullConstructor_withNullUI_allowsNullUI() {
		assertDoesNotThrow(() -> {
			new GameContext(mockTurnManager, mockPlayerManager, mockDeck,
					mockCurrentPlayer, null, mockCardFactory);
		});
	}

	@Test
	void addTurnForCurrentPlayer_withFullContext_callsTurnManager() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);

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
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);

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
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);

		mockTurnManager.endTurnWithoutDrawForAttacks();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockTurnManager);

		fullGameContext.endTurnWithoutDrawingForAttacks();

		EasyMock.verify(mockTurnManager);
	}

	@Test
	void viewTopTwoCardsFromDeck_emptyDeck_throwsNoSuchElementException() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		mockDeck.peekTopTwoCards();
		EasyMock.expectLastCall()
				.andThrow(new NoSuchElementException("Deck is empty"));
		EasyMock.replay(mockDeck);
		assertThrows(NoSuchElementException.class,
				() -> fullGameContext.viewTopTwoCardsFromDeck());
		EasyMock.verify(mockDeck);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	void viewTopTwoCardsFromDeck_deckWithOneCard_returnsTheOnlyCard(CardType testCardType) {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		List<Card> expectedCardList = List.of(mockCard(testCardType));
		EasyMock.expect(mockDeck.peekTopTwoCards()).andReturn(expectedCardList);
		EasyMock.replay(mockDeck);
		List<Card> actualCardList = fullGameContext.viewTopTwoCardsFromDeck();
		assertEquals(expectedCardList, actualCardList);
		EasyMock.verify(mockDeck);
	}

	@Test
	void viewTopTwoCardsFromDeck_deckWithTwoCards_returnsTwoLastCards() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		Card card1 = mockCard(CardType.NORMAL);
		Card card2 = mockCard(CardType.FAVOR);
		List<Card> expectedCardList = List.of(card1, card2);
		EasyMock.expect(mockDeck.peekTopTwoCards()).andReturn(expectedCardList);
		EasyMock.replay(mockDeck);
		List<Card> actualCardList = fullGameContext.viewTopTwoCardsFromDeck();
		assertEquals(expectedCardList, actualCardList);
		EasyMock.verify(mockDeck);
	}

	@Test
	void viewTopTwoCardsFromDeck_deckWithThreeCardsAndDuplicate_returnsDuplicates() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		Card duplicateCard1 = mockCard(CardType.SKIP);
		Card duplicateCard2 = mockCard(CardType.SKIP);
		List<Card> expectedCardList = List.of(duplicateCard1, duplicateCard2);
		EasyMock.expect(mockDeck.peekTopTwoCards()).andReturn(expectedCardList);
		EasyMock.replay(mockDeck);
		List<Card> actualCardList = fullGameContext.viewTopTwoCardsFromDeck();
		assertEquals(expectedCardList, actualCardList);
		EasyMock.verify(mockDeck);
	}

	@Test
	void shuffleDeckFromDeck_withFullContext_callShuffleDeck() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);

		mockDeck.shuffleDeck(EasyMock.anyObject(Random.class));
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockDeck);

		fullGameContext.shuffleDeckFromDeck();

		EasyMock.verify(mockDeck);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	void transferCardBetweenPlayers_withCardNotInHand_throwsIllegalArgumentException(
			CardType testCardType) {
		EasyMock.expect(mockPlayerManager.getNumberOfPlayers())
				.andReturn(DEFAULT_PLAYERS);

		int playerIndex = 1;
		Player mockPlayerGiver = EasyMock.createMock(Player.class);
		EasyMock.expect(userInterface.getNumericUserInput(
				"Enter the index of a player you want to get card from (0, 2)",
						0, 2))
				.andReturn(playerIndex);
		EasyMock.expect(mockPlayerManager.getPlayerByIndex(playerIndex))
				.andReturn(mockPlayerGiver);


		Card testCard = mockCard(testCardType);
		String cardTypeInput = "testCardType";
		EasyMock.expect(userInterface.getUserInput(
				"Enter card type you want to give to current player"))
				.andReturn(cardTypeInput);
		EasyMock.expect(mockPlayerGiver.parseCardType(cardTypeInput))
				.andReturn(testCardType);
		EasyMock.expect(mockCardFactory.createCard(testCardType))
				.andReturn(testCard);

		mockPlayerGiver.removeCardFromHand(testCard);
		EasyMock.expectLastCall()
				.andThrow(new IllegalArgumentException
						("Card not in hand: can not remove card"));

		EasyMock.replay(mockPlayerGiver, mockCardFactory, userInterface, mockPlayerManager);

		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		assertThrows(IllegalArgumentException.class,
				fullGameContext::transferCardBetweenPlayers);

		EasyMock.verify(mockPlayerGiver,
				userInterface, mockCardFactory, mockPlayerManager);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	void transferCardBetweenPlayers_withCardInHand_transfersCard(CardType testCardType) {
		EasyMock.expect(mockPlayerManager.getNumberOfPlayers())
				.andReturn(DEFAULT_PLAYERS);

		int playerIndex = 1;
		Player mockPlayerGiver = EasyMock.createMock(Player.class);
		String playerInputMessage =
				"Enter the index of a player you want to get card from (0, 2)";
		EasyMock.expect(userInterface.getNumericUserInput(
						playerInputMessage,
						0, 2))
				.andReturn(playerIndex);
		EasyMock.expect(mockPlayerManager.getPlayerByIndex(playerIndex))
				.andReturn(mockPlayerGiver);


		Card testCard = mockCard(testCardType);
		String cardTypeInput = "testCardType";
		EasyMock.expect(userInterface.getUserInput(
				"Enter card type you want to give to current player"))
				.andReturn(cardTypeInput);
		EasyMock.expect(mockPlayerGiver.parseCardType(cardTypeInput))
				.andReturn(testCardType);
		EasyMock.expect(mockCardFactory.createCard(testCardType))
				.andReturn(testCard);

		mockPlayerGiver.removeCardFromHand(testCard);
		EasyMock.expectLastCall().once();

		mockCurrentPlayer.addCardToHand(testCard);
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockPlayerGiver, mockCurrentPlayer,
				userInterface, mockCardFactory, mockPlayerManager);

		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		fullGameContext.transferCardBetweenPlayers();

		EasyMock.verify(mockPlayerGiver, mockCurrentPlayer,
				userInterface, mockCardFactory, mockPlayerManager);
	}

	private Card mockCard(CardType cardType) {
		Card mockCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
		EasyMock.replay(mockCard);
		return mockCard;
	}

}
