package domain;

public class CardFactory {

    public Card createCard(CardType type) {
        return new Card(type);
    }
}

