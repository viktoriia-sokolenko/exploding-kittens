package domain;

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
			case FAVOR:
				return new FavorCard();
			default: throw new IllegalArgumentException("Unknown card type: " + type);
		}
	}
}

