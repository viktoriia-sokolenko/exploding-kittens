package domain;

public class CardFactory {

    public Card createCard(CardType type) {
        switch (type) {
            case EXPLODING_KITTEN:
                return new ExpoldingKittenCard();
            default: return null;
        }
    }
}

