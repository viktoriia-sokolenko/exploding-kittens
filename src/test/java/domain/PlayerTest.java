package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

	@Test
	public void getCardTypeCount_withNullCardType_throwsNullPointerException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(null)).andThrow(new NullPointerException("CardType cannot be null"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(NullPointerException.class, () -> player.getCardTypeCount(null));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void getCardTypeCount_withCardNotInHand_returnsZero(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(testCardType)).andReturn(0);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertEquals (0, player.getCardTypeCount(testCardType));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void getCardTypeCount_withOneCardInHand_returnsOne(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(testCardType)).andReturn(1);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertEquals (1, player.getCardTypeCount(testCardType));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void getCardTypeCount_withTwoCardsInHand_returnsTwo(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getCountOfCardType(testCardType)).andReturn(2);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertEquals (2, player.getCardTypeCount(testCardType));

		EasyMock.verify(mockHand);
	}

	@Test
	public void drawCard_withEmptyDeck_throwsNoSuchElementException(){
		Deck mockDeck = EasyMock.createMock(Deck.class);
		EasyMock.expect(mockDeck.draw()).andThrow(new NoSuchElementException("Deck is empty"));
		EasyMock.replay(mockDeck);

		Hand mockHand = EasyMock.createMock(Hand.class);

		Player player = new Player(mockHand);
		assertThrows(NoSuchElementException.class, () -> player.drawCard(mockDeck));

		EasyMock.verify(mockDeck);
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void drawCard_withNonEmptyDeck_addsCardToHand(CardType testCardType) {
		Card mockCard = mockCard(testCardType);

		Deck mockDeck = EasyMock.createMock(Deck.class);
		EasyMock.expect(mockDeck.draw()).andReturn(mockCard);
		EasyMock.replay(mockDeck);

		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.addCard(mockCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		player.drawCard(mockDeck);

		EasyMock.verify(mockDeck);
		EasyMock.verify(mockHand);
	}

	@Test
	public void drawExplodingKittenCard_withDefuseInHand_removesDefuseFromHand() {
		//TODO: still need to figure out how to mock defuseCard because mock and actual defuse cards are not perceived
		// as equal arguments by EasyMock when using expectLastCall
		Card defuseCard = new Card(CardType.DEFUSE);
		Card explodingKittenMockCard = mockCard(CardType.EXPLODING_KITTEN);

		Deck mockDeck = mockDeck(explodingKittenMockCard);

		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(CardType.DEFUSE)).andReturn(true);
		mockHand.removeCard(defuseCard);
		EasyMock.expectLastCall();
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		player.drawCard(mockDeck);

		EasyMock.verify(mockDeck);
		EasyMock.verify(mockHand);
	}

	@Test
	public void drawExplodingKittenCard_withDefuseNotInHand_removesPlayer() {
		Card explodingKittenMockCard = mockCard(CardType.EXPLODING_KITTEN);

		Deck mockDeck = mockDeck(explodingKittenMockCard);

		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(CardType.DEFUSE)).andReturn(false);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		player.drawCard(mockDeck);

		assertFalse(player.isInGame());

		EasyMock.verify(mockDeck);
		EasyMock.verify(mockHand);
	}

	@Test
	public void playCard_withNullCard_throwsNullPointerException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeCard(null);
		EasyMock.expectLastCall().andThrow(new NullPointerException("Card cannot be null"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(NullPointerException.class, () -> player.playCard(null));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void playCard_withEmptyHand_throwsIllegalStateException(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeCard(EasyMock.anyObject(Card.class));
		EasyMock.expectLastCall().andThrow(new IllegalStateException("Hand empty: can not remove card"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(IllegalStateException.class, () -> player.playCard(mockCard(testCardType)));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void playCard_withCardNotInHand_throwsIllegalArgumentException(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeCard(EasyMock.anyObject(Card.class));
		EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Card not in hand: can not remove card"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(IllegalArgumentException.class, () -> player.playCard(mockCard(testCardType)));

		EasyMock.verify(mockHand);
	}

	private Card mockCard(CardType cardType) {
		Card mockCard = EasyMock.createMock(Card.class);
		EasyMock.expect(mockCard.getCardType()).andStubReturn(cardType);
		EasyMock.replay(mockCard);
		return mockCard;
	}

	private Deck mockDeck(Card card) {
		Deck mockDeck = EasyMock.createMock(Deck.class);
		EasyMock.expect(mockDeck.draw()).andReturn(card);
		EasyMock.replay(mockDeck);
		return mockDeck;
	}
}
