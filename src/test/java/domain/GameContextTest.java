package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ui.UserInterface;

import java.util.ArrayList;
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
	public void constructor_withValidPlayer_createsGameContext() {
		assertNotNull(gameContext);
	}

	@Test
	public void constructor_withNullPlayer_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(null);
		});
	}

	@Test
	public void getCurrentPlayer_returnsCurrentPlayer() {
		Player result = gameContext.getCurrentPlayer();
		assertEquals(mockCurrentPlayer, result);
	}

	@Test
	public void endTurnWithoutDrawing_doesNotThrowException() {
		assertDoesNotThrow(() -> gameContext.endTurnWithoutDrawing());
	}

	@Test
	public void endTurnWithoutDrawingForAttacks_doesNotThrowException() {
		assertDoesNotThrow(() -> gameContext.endTurnWithoutDrawingForAttacks());
	}

	@Test
	public void constructor_withValidParameters_createsGameContext() {
		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);
		assertNotNull(fullGameContext);
		assertEquals(mockCurrentPlayer, fullGameContext.getCurrentPlayer());
	}

	@Test
	public void constructor_withNullTurnManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(null, mockPlayerManager,
					mockDeck, mockCurrentPlayer,
					userInterface, mockCardFactory);
		});
	}

	@Test
	public void constructor_withNullPlayerManager_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, null, mockDeck,
					mockCurrentPlayer, userInterface, mockCardFactory);
		});
	}

	@Test
	public void constructor_withNullDeck_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, mockPlayerManager, null,
					mockCurrentPlayer, userInterface, mockCardFactory);
		});
	}

	@Test
	public void constructor_withNullCurrentPlayer_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> {
			new GameContext(mockTurnManager, mockPlayerManager, mockDeck,
					null, userInterface, mockCardFactory);
		});
	}

	@Test
	public void fullConstructor_withNullUI_allowsNullUI() {
		assertDoesNotThrow(() -> {
			new GameContext(mockTurnManager, mockPlayerManager, mockDeck,
					mockCurrentPlayer, null, mockCardFactory);
		});
	}

	@Test
	public void addTurnForCurrentPlayer_withFullContext_callsTurnManager() {
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
	public void addTurnForCurrentPlayer_withSimpleContext_doesNotThrowException() {
		assertDoesNotThrow(() -> gameContext.addTurnForCurrentPlayer());
	}

	@Test
	public void endTurnWithoutDrawing_withFullContext_callsTurnManager() {
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
	public void endTurnWithoutDrawingForAttacks_withFullContext_callsTurnManager() {
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
	public void viewTopTwoCardsFromDeck_emptyDeck_throwsNoSuchElementException() {
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
	public void viewTopTwoCardsFromDeck_deckWithOneCard_returnsTheOnlyCard(
			CardType testCardType) {
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
	public void viewTopTwoCardsFromDeck_deckWithTwoCards_returnsTwoLastCards() {
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
	public void viewTopTwoCardsFromDeck_deckWithThreeCardsAndDuplicate_returnsDuplicates() {
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
	public void shuffleDeckFromDeck_withFullContext_callShuffleDeck() {
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
	public void transferCardBetweenPlayers_withCardNotInHand_throwsIllegalArgumentException(
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
	public void transferCardBetweenPlayers_withCardInHand_transfersCard(CardType testCardType) {
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

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void rearrangeTopThreeCardsFromDeck_oneCardDeck_callsRearrangeTopThreeCards(
			CardType testCardType
	) {
		Card mockCard = mockCard(testCardType);
		List<Card> oneCardList = new ArrayList<>(List.of(mockCard));

		EasyMock.expect(mockDeck.peekTopThreeCards())
				.andReturn(oneCardList);
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(1).anyTimes();

		userInterface.displayCardsFromDeck(oneCardList, 1);
		EasyMock.expectLastCall().once();

		String messageForPlayer = "Enter the index of a card " +
						"that you want to put in position 0 " +
						"starting from the top of the Deck.\n" +
						"Only possible indexes are from 0 to 0.";
		EasyMock.expect(userInterface.getNumericUserInput(
				messageForPlayer, 0, 0)).andReturn(0);

		List<Integer> indices = List.of(0);
		mockDeck.rearrangeTopThreeCards(indices);
		EasyMock.expectLastCall().once();

		EasyMock.replay(mockDeck, userInterface);

		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);

		fullGameContext.rearrangeTopThreeCardsFromDeck();
		EasyMock.verify(mockDeck, userInterface);
	}

	@Test
	public void rearrangeTopThreeCardsFromDeck_sameIndices_throwsIllegalArgumentException() {
		Card card1 = mockCard(CardType.NORMAL);
		Card card2 = mockCard(CardType.ATTACK);
		List<Card> twoCardList = new ArrayList<>(List.of(card1, card2));

		EasyMock.expect(mockDeck.peekTopThreeCards())
				.andReturn(twoCardList);
		EasyMock.expect(mockDeck.getDeckSize()).andReturn(2).anyTimes();

		userInterface.displayCardsFromDeck(twoCardList, 2);
		EasyMock.expectLastCall().once();

		String message1 = "Enter the index of a card " +
				"that you want to put in position 0 " +
				"starting from the top of the Deck.\n" +
				"Only possible indexes are from 0 to 1.";
		EasyMock.expect(userInterface.getNumericUserInput(
						message1, 0, 1)).andReturn(1);

		String message2 = "Enter the index of a card " +
				"that you want to put in position 1 " +
				"starting from the top of the Deck.\n" +
				"Only possible indexes are from 0 to 1.";
		EasyMock.expect(userInterface.getNumericUserInput(
				message2, 0, 1)).andReturn(1);

		List<Integer> duplicateIndices = List.of(1, 1);
		mockDeck.rearrangeTopThreeCards(duplicateIndices);
		EasyMock.expectLastCall().andThrow(new IllegalArgumentException(
				"Duplicate indices are not allowed"));

		EasyMock.replay(mockDeck, userInterface);

		GameContext fullGameContext = new GameContext(mockTurnManager,
				mockPlayerManager,
				mockDeck, mockCurrentPlayer, userInterface, mockCardFactory);

		assertThrows(IllegalArgumentException.class, () -> {
			fullGameContext.rearrangeTopThreeCardsFromDeck();
		});
		EasyMock.verify(mockDeck, userInterface);
	}

	private Card mockCard(CardType cardType) {
		Card mockCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
		EasyMock.replay(mockCard);
		return mockCard;
	}

}
