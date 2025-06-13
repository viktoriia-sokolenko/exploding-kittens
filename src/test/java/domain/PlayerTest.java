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
		EasyMock.expect(mockHand.getCountOfCardType(null))
				.andThrow(new NullPointerException("CardType cannot be null"));
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
	public void drawCard_withEmptyDeck_throwsNoSuchElementException() {
		Deck mockDeck = EasyMock.createMock(Deck.class);
		EasyMock.expect(mockDeck.draw())
				.andThrow(new NoSuchElementException("Deck is empty"));
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
	public void drawExplodingKittenCard_withDefuseInHand_insertsDrawnCardBackIntoDeck() {
		Card explodingKittenMockCard = mockCard(CardType.EXPLODING_KITTEN);

		Deck mockDeck = mockDeck(explodingKittenMockCard);
		int indexToInsertAt = 1;
		mockDeck.insertCardAt(explodingKittenMockCard, indexToInsertAt);
		EasyMock.expectLastCall();
		EasyMock.replay(mockDeck);

		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(CardType.DEFUSE)).andReturn(true);
		mockHand.removeDefuseCard();
		EasyMock.expectLastCall();
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		player.drawCard(mockDeck);

		assertTrue(player.isInGame());

		EasyMock.verify(mockDeck);
		EasyMock.verify(mockHand);
	}

	@Test
	public void drawExplodingKittenCard_withDefuseNotInHand_removesPlayer() {
		Card explodingKittenMockCard = mockCard(CardType.EXPLODING_KITTEN);

		Deck mockDeck = mockDeck(explodingKittenMockCard);
		EasyMock.replay(mockDeck);

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
	public void removeCardFromHand_withNullCard_throwsNullPointerException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeCard(null);
		EasyMock.expectLastCall().andThrow(new NullPointerException("Card cannot be null"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(NullPointerException.class, () -> player.removeCardFromHand(null));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void removeCardFromHand_withEmptyHand_throwsIllegalStateException
			(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeCard(EasyMock.anyObject(Card.class));
		EasyMock.expectLastCall()
				.andThrow(new IllegalStateException
						("Hand empty: can not remove card"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(IllegalStateException.class,
				() -> player.removeCardFromHand(mockCard(testCardType)));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void removeCardFromHand_withCardNotInHand_throwsIllegalArgumentException
			(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeCard(EasyMock.anyObject(Card.class));
		EasyMock.expectLastCall()
				.andThrow(new IllegalArgumentException
						("Card not in hand: can not remove card"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(IllegalArgumentException.class,
				() -> player.removeCardFromHand(mockCard(testCardType)));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN"}, mode = EnumSource.Mode.EXCLUDE)
	public void removeCardFromHand_withCardInHand_removesCard
			(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeCard(EasyMock.anyObject(Card.class));
		EasyMock.expectLastCall();
		EasyMock.replay(mockHand);

		Card testCard = mockCard(testCardType);
		Player player = new Player(mockHand);
		player.removeCardFromHand(testCard);

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
		return mockDeck;
	}

	@Test
	public void getNumberOfCards_returnsNumberOfCards() {
		final int NUMBER_OF_CARDS = 5;
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getNumberOfCards())
				.andReturn(NUMBER_OF_CARDS);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		int result = player.getNumberOfCards();

		assertEquals(NUMBER_OF_CARDS, result);
		EasyMock.verify(mockHand);
	}

	@Test
	public void getNumberOfCards_withManyCards_returnsCorrectCount() {
		final int NUMBER_OF_CARDS = 15;
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.getNumberOfCards())
				.andReturn(NUMBER_OF_CARDS);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		int result = player.getNumberOfCards();
		assertEquals(NUMBER_OF_CARDS, result);
		EasyMock.verify(mockHand);
	}

	@Test
	public void hasCardType_withNullCardType_throwsNullPointerException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(null))
				.andThrow(new NullPointerException("CardType cannot be null"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(NullPointerException.class, () -> player.hasCardType(null));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void hasCardType_withCardNotInHand_returnsFalse(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(testCardType))
				.andReturn(false);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertFalse(player.hasCardType(testCardType));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(CardType.class)
	public void hasCardType_withCardInHand_returnsTrue(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(testCardType))
				.andReturn(true);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertTrue(player.hasCardType(testCardType));

		EasyMock.verify(mockHand);
	}

	@Test
	public void hasCardType_withDefuseCard_returnsTrue() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(CardType.DEFUSE))
				.andReturn(true);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertTrue(player.hasCardType(CardType.DEFUSE));

		EasyMock.verify(mockHand);
	}

	@Test
	public void hasCardType_withExplodingKitten_returnsTrue() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.containsCardType(CardType.EXPLODING_KITTEN))
				.andReturn(true);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertTrue(player.hasCardType(CardType.EXPLODING_KITTEN));

		EasyMock.verify(mockHand);
	}

	@Test
	public void addCardToHand_withNullCard_throwsNullPointerException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.addCard(null);
		EasyMock.expectLastCall().andThrow
				(new NullPointerException("Card cannot be null"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(NullPointerException.class, ()
				-> player.addCardToHand(null));

		EasyMock.verify(mockHand);
	}

	@ParameterizedTest
	@EnumSource(value = CardType.class,
			names = {"EXPLODING_KITTEN", "UNKNOWN_CARD_FOR_TEST"},
			mode = EnumSource.Mode.EXCLUDE)
	public void addCardToHand_withValidCard_addsCardToHand(CardType testCardType) {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.addCard(EasyMock.anyObject(Card.class));
		EasyMock.expectLastCall();
		EasyMock.replay(mockHand);

		Card testCard = mockCard(testCardType);
		Player player = new Player(mockHand);
		player.addCardToHand(testCard);

		EasyMock.verify(mockHand);
	}

	@Test
	public void addCardToHand_withExplodingKittenCard_throwsIllegalArgumentException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.addCard(EasyMock.anyObject(Card.class));
		EasyMock.expectLastCall().andThrow(new IllegalArgumentException(
				"Exploding Kitten should not be added to Hand"));
		EasyMock.replay(mockHand);

		Card explodingKittenCard = mockCard(CardType.EXPLODING_KITTEN);
		Player player = new Player(mockHand);
		assertThrows(IllegalArgumentException.class,
				() -> player.addCardToHand(explodingKittenCard));

		EasyMock.verify(mockHand);
	}

	@Test
	public void removeDefuseCard_withEmptyHand_throwsIllegalStateException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeDefuseCard();
		EasyMock.expectLastCall().andThrow(new IllegalStateException(
				"Hand empty: can not remove card"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(IllegalStateException.class, player::removeDefuseCard);

		EasyMock.verify(mockHand);
	}

	@Test
	public void removeDefuseCard_withNoDefuseCardInHand_throwsIllegalArgumentException() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeDefuseCard();
		EasyMock.expectLastCall().andThrow(new IllegalArgumentException(
				"Card not in hand: can not remove card"));
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		assertThrows(IllegalArgumentException.class, player::removeDefuseCard);

		EasyMock.verify(mockHand);
	}

	@Test
	public void removeDefuseCard_withDefuseCardInHand_removesDefuseCard() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		mockHand.removeDefuseCard();
		EasyMock.expectLastCall();
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		player.removeDefuseCard();

		EasyMock.verify(mockHand);
	}

	@Test
	public void parseCardType_withNullInput_returnsNull() {
		Hand mockHand = EasyMock.createMock(Hand.class);
		EasyMock.expect(mockHand.parseCardType(null)).andReturn(null);
		EasyMock.replay(mockHand);

		Player player = new Player(mockHand);
		CardType result = player.parseCardType(null);
		assertNull(result);

		EasyMock.verify(mockHand);
	}
}
