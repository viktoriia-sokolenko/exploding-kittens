package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class CardFactoryTest {

	private static final int DEFUSE_BATCH_SIZE = 5;
	private static final int FIRST_BATCH_SIZE = 3;
	private static final int SECOND_BATCH_SIZE = 2;
	private static final int LARGE_BATCH_SIZE = 100;

	@Test
	public void createCard_withExplodingKittenCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card explodingKitten = factory.createCard(CardType.EXPLODING_KITTEN);
		assertInstanceOf(ExpoldingKittenCard.class, explodingKitten);
		assertEquals(CardType.EXPLODING_KITTEN, explodingKitten.getCardType());
	}

	@Test
	public void createCard_withNullType_throwsNullPointerException() {
		CardFactory factory = new CardFactory();
		assertThrows(NullPointerException.class, () -> factory.createCard(null));
	}

	@Test
	public void createCard_withDefuseCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card defuseCard = factory.createCard(CardType.DEFUSE);
		assertInstanceOf(DefuseCard.class, defuseCard);
		assertEquals(CardType.DEFUSE, defuseCard.getCardType());
	}

	@Test
	public void createCard_withUnknownCardType_throwsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class,
				() -> factory.createCard(CardType.UNKNOWN_CARD_FOR_TEST));
	}

	@Test
	public void createCard_withNormalCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.NORMAL);
		assertInstanceOf(NormalCard.class, card);
		assertEquals(CardType.NORMAL, card.getCardType());
	}

	@Test
	public void createCard_withAttackCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.ATTACK);
		assertInstanceOf(AttackCard.class, card);
		assertEquals(CardType.ATTACK, card.getCardType());
	}

	@Test
	public void createCard_withSkipCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.SKIP);
		assertInstanceOf(SkipCard.class, card);
		assertEquals(CardType.SKIP, card.getCardType());
	}

	@Test
	public void createCard_withFavorCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.FAVOR);
		assertInstanceOf(FavorCard.class, card);
		assertEquals(CardType.FAVOR, card.getCardType());
	}

	@Test
	public void createCard_withAlterTheFutureCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.ALTER_THE_FUTURE);
		assertInstanceOf(AlterTheFutureCard.class, card);
		assertEquals(CardType.ALTER_THE_FUTURE, card.getCardType());
	}

	@Test
	public void createCard_withSeeTheFutureCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.SEE_THE_FUTURE);
		assertInstanceOf(SeeTheFutureCard.class, card);
		assertEquals(CardType.SEE_THE_FUTURE, card.getCardType());
	}

	@Test
	public void createCard_withShuffleCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.SHUFFLE);
		assertInstanceOf(ShuffleCard.class, card);
		assertEquals(CardType.SHUFFLE, card.getCardType());
	}

	@Test
	public void createCard_withReverseCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.REVERSE);
		assertInstanceOf(ReverseCard.class, card);
		assertEquals(CardType.REVERSE, card.getCardType());
	}

	@Test
	public void createCard_withNukeCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.NUKE);
		assertInstanceOf(NukeCard.class, card);
		assertEquals(CardType.NUKE, card.getCardType());
	}

	@Test
	public void createCard_withBuryCardType_createsCard() {
		CardFactory factory = new CardFactory();
		Card card = factory.createCard(CardType.BURY);
		assertInstanceOf(BuryCard.class, card);
		assertEquals(CardType.BURY, card.getCardType());
	}

	@Test
	public void createCards_withNullTypeAndValidCount_throwsNullPointerException() {
		CardFactory factory = new CardFactory();
		assertThrows(NullPointerException.class,
				() -> factory.createCards(null, 1));
	}

	@Test
	public void createCards_withNegativeCount_throwsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class,
				() -> factory.createCards(CardType.NORMAL, -1));
	}

	@Test
	public void createCards_withZeroCount_throwsIllegalArgumentException() {
		CardFactory factory = new CardFactory();
		assertThrows(IllegalArgumentException.class,
				() -> factory.createCards(CardType.NORMAL, 0));
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
		List<Card> cards = factory.createCards(CardType.DEFUSE, DEFUSE_BATCH_SIZE);
		assertEquals(DEFUSE_BATCH_SIZE, cards.size());
		cards.forEach(c -> assertInstanceOf(DefuseCard.class, c));
	}

	@Test
	public void createCards_withMultipleDifferentTypes_returnsCorrectTypeForEachCardType() {
		CardFactory factory = new CardFactory();
		Card normal	   = factory.createCard(CardType.NORMAL);
		Card exploding = factory.createCard(CardType.EXPLODING_KITTEN);
		Card defuse	   = factory.createCard(CardType.DEFUSE);

		assertInstanceOf(NormalCard.class, normal);
		assertInstanceOf(ExpoldingKittenCard.class, exploding);
		assertInstanceOf(DefuseCard.class, defuse);

		assertEquals(CardType.NORMAL, normal.getCardType());
		assertEquals(CardType.EXPLODING_KITTEN, exploding.getCardType());
		assertEquals(CardType.DEFUSE, defuse.getCardType());
	}

	@Test
	public void createCards_sequentialCalls_returnIndependentResults() {
		CardFactory factory = new CardFactory();

		List<Card> batch1 = factory.createCards(CardType.NORMAL, FIRST_BATCH_SIZE);
		List<Card> batch2 = factory.createCards(CardType.EXPLODING_KITTEN,
				SECOND_BATCH_SIZE);

		assertEquals(FIRST_BATCH_SIZE, batch1.size());
		assertEquals(SECOND_BATCH_SIZE, batch2.size());

		batch1.forEach(c -> assertInstanceOf(NormalCard.class, c));
		batch2.forEach(c -> assertInstanceOf(ExpoldingKittenCard.class, c));
	}

	@Test
	public void createCards_withLargeNumber_createsCorrectNumberOfCards() {
		CardFactory factory = new CardFactory();
		List<Card> cards = factory.createCards(CardType.NORMAL, LARGE_BATCH_SIZE);
		assertEquals(LARGE_BATCH_SIZE, cards.size());
	}
}
