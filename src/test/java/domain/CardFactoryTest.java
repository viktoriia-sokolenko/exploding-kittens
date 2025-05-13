package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class CardFactoryTest {
	@Test
	public void createCard_WithNullType_ThrowsNullPointerException() {
		CardFactory factory = new CardFactory();
		Card expoldingKittenCard = factory.createCard(CardType.EXPLODING_KITTEN);
		assertInstanceOf(ExpoldingKittenCard.class, expoldingKittenCard);
	}

	@Test
	public void CreateCard_WithDefuseCardType_CreatesCard() {
		CardFactory factory = new CardFactory();
		Card defuseCard = factory.createCard(CardType.DEFUSE);
		assertInstanceOf(DefuseCard.class, defuseCard);
	}

	@Test public void CreateCard_WithUnknownCardType_ThrowsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class, () ->
				factory.createCard(CardType.UNKNOWN_CARD_FOR_TEST));
	}

	@Test public void CreateCard_WithNormalCardType_CreatesCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.NORMAL);
		assertInstanceOf(Card.class, card);
	}

	@Test public void CreateCard_WithAttackCardType_CreatesCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard((CardType.ATTACK));
		assertInstanceOf(Card.class, card);
	}

	@Test public void CreateCard_WithSkipCardType_CreatesCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.SKIP);
		assertInstanceOf(Card.class, card);
	}

	@Test public void CreateCard_WithFavorCardType_CreatesCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.FAVOR);
		assertInstanceOf(Card.class, card);
	}

	@Test public void CreateCard_WithShieldCardType_CreatesCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.SHUFFLE);
		assertInstanceOf(Card.class, card);
	}

	@Test public void CreateCard_WithSeeIntoTheFutureCardType_CreateCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.SEE_THE_FUTURE);
		assertInstanceOf(Card.class, card);
	}

	@Test public void CreateCard_WithAlterTheFutureCardType_CreateCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.ALTER_THE_FUTURE);
		assertInstanceOf(Card.class, card);
	}

	@Test public void CreateCard_WithNukeCardType_CreateCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.NUKE);
		assertInstanceOf(Card.class, card);
	}

	@Test
	public void createCards_withNegativeCount_throwsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class, () ->
				factory.createCards(CardType.NORMAL, -1));
	}

	@Test
	public void  createCards_withZeroCount_throwsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class, () ->
				factory.createCards(CardType.NORMAL, 0));
	}

	@Test
	public void createCards_withValidTypeAndCountOne_returnsListWithOneCard() {
		CardFactory factory = new CardFactory();
		List<Card> cards = factory.createCards(CardType.NORMAL, 1);

		assertEquals(1, cards.size());
		assertInstanceOf(NormalCard.class, cards.get(0));
	}

	@Test
	public void createCards_withValidTypeAndCountGreaterThanOne_returnsList() {
		CardFactory factory = new CardFactory();
		int numCards = 5;
		List<Card> cards = factory.createCards(CardType.DEFUSE, numCards);

		assertEquals(numCards, cards.size());
		for (Card card : cards) {
			assertInstanceOf(DefuseCard.class, card);
		}
	}

	@Test
	public void createCards_withValidTypeAndCountGreaterThanOne_returnsLists() {
		CardFactory factory = new CardFactory();
		List<CardType> types = List.of(
				CardType.ATTACK,
				CardType.DEFUSE,
				CardType.NORMAL,
				CardType.EXPLODING_KITTEN,
				CardType.SKIP
		);

		List<Card> cards = new ArrayList<>();
		for (CardType type : types) {
			cards.add(factory.createCard(type));
		}

		Set<Card> uniqueCards = new HashSet<>(cards);
		assertEquals(types.size(), uniqueCards.size()
				, "All cards should be unique instances");
	}

	@Test
	public void createCards_sequentialCalls_returnIndependentResults() {
		CardFactory factory = new CardFactory();
		int firstBatchSize = 3;
		List<Card> firstBatch = factory.createCards(CardType.NORMAL, firstBatchSize);
		assertEquals(firstBatchSize, firstBatch.size());

		int secondBatchSize = 2;
		List<Card> secondBatch = factory.createCards(CardType.EXPLODING_KITTEN,
				secondBatchSize);
		assertEquals(secondBatchSize, secondBatch.size());

		assertEquals(firstBatchSize, firstBatch.size());

		for (Card card : firstBatch) {
			assertInstanceOf(NormalCard.class, card);
		}

		for (Card card : secondBatch) {
			assertInstanceOf(ExpoldingKittenCard.class, card);
		}
	}

	@Test
	public void createCards_withLargeNumber_createsCorrectNumberOfCards() {
		CardFactory factory = new CardFactory();
		int numCards = 100;
		List<Card> cards = factory.createCards(CardType.NORMAL, numCards);

		assertEquals(numCards, cards.size());
	}

	@Test
	void createCard_returnsCorrectTypeForEachCardType() {
		CardFactory factory = new CardFactory();

		Card normalCard = factory.createCard(CardType.NORMAL);
		Card explodingCard = factory.createCard(CardType.EXPLODING_KITTEN);
		Card defuseCard = factory.createCard(CardType.DEFUSE);

		assertInstanceOf(NormalCard.class, normalCard);
		assertInstanceOf(ExpoldingKittenCard.class, explodingCard);
		assertInstanceOf(DefuseCard.class, defuseCard);

		assertEquals(CardType.NORMAL, normalCard.getCardType());
		assertEquals(CardType.EXPLODING_KITTEN, explodingCard.getCardType());
		assertEquals(CardType.DEFUSE, defuseCard.getCardType());
	}
}