package domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CardFactoryTest {

	@Test
	public void CreateCard_WithNullType_ThrowsNullPointerException() {
		CardFactory factory = new CardFactory();
		assertThrows(NullPointerException.class, () -> factory.createCard(null));
	}

	@Test
	public void CreateCard_WithExplodingKittenCardType_CreatesCard() {
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
	void createCards_withNegativeCount_throwsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class, () ->
				factory.createCards(CardType.NORMAL, -1));
	}

	@Test
	void createCards_withZeroCount_throwsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class, () ->
				factory.createCards(CardType.NORMAL, 0));
	}

	@Test
	void createCards_withValidTypeAndCountOne_returnsListWithOneCard() {
		CardFactory factory = new CardFactory();
		List<Card> cards = factory.createCards(CardType.NORMAL, 1);

		assertEquals(1, cards.size());
		assertInstanceOf(NormalCard.class, cards.get(0));
	}

	@Test
	void createCards_withValidTypeAndCountGreaterThanOne_returnsListWithCorrectNumberOfCards() {
		CardFactory factory = new CardFactory();
		int numCards = 5;
		List<Card> cards = factory.createCards(CardType.DEFUSE, numCards);

		assertEquals(numCards, cards.size());
		for (Card card : cards) {
			assertInstanceOf(DefuseCard.class, card);
		}
	}

}