package domain;

import java.util.Objects;

public abstract class Card {
	private final CardType cardType;

	public Card(CardType cardType) {
		this.cardType = Objects.requireNonNull(cardType, "Card type cannot be null");
	}

	public CardType getCardType() {
		return cardType;
	}

	public abstract void play(Player player);

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

	public @Override int hashCode() {
		return Objects.hash(cardType);
	}
}


