package domain;

public class CardFactory {

    public Card createCard(CardType type) {
        switch (type) {
            case EXPLODING_KITTEN:
                return new ExpoldingKittenCard();
            case DEFUSE:
                return new DefuseCard();
            default: throw new IllegalArgumentException("Unknown card type: " + type);
        }
    }
}

