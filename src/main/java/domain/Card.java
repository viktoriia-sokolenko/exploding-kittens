package domain;

import java.util.Objects;

public class Card {
    private final CardType cardType;

    public Card(CardType cardType) {
        this.cardType = Objects.requireNonNull(cardType, String.format("Card type cannot be null"));
    }

    public CardType getCardType() {
        return cardType;
    }

    public @Override boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card other = (Card) obj;
        return cardType == other.cardType;
    }
}


