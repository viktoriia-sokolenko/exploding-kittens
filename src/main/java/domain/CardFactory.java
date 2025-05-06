package domain;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {

	public Card createCard(CardType type) {
		switch (type) {
			case NORMAL:
				return new NormalCard();
			case EXPLODING_KITTEN:
				return new ExpoldingKittenCard();
			case DEFUSE:
				return new DefuseCard();
			case ATTACK:
				return new AttackCard();
			case SKIP:
				return new SkipCard();
			case ALTER_THE_FUTURE:
				return new AlterTheFutureCard();
			case SEE_THE_FUTURE:
				return new SeeTheFutureCard();
			case SHUFFLE:
				return new ShuffleCard();
			case NUKE:
				return new NukeCard();
			case FAVOR:
				return new FavorCard();
			default: throw new IllegalArgumentException("Unknown card type: " + type);
		}
	}

	public List<Card> createCards(CardType type, int numberOfCards) {
		if (numberOfCards <= 0) {
			throw new
					IllegalArgumentException("Number of cards must be above 0");
		}
		List<Card> cards = new ArrayList<>(numberOfCards);
		for (int i = 0; i < numberOfCards; i++) {
			cards.add(createCard(type));
		}

		return cards;
	}
}

