package domain;

import java.util.Objects;

public class Card {
	private final CardType cardType;

	@SuppressWarnings("checkstyle:FileTabCharacter")
	public Card(CardType cardType) {
		this.cardType = Objects.requireNonNull(cardType, "Card type cannot be null");
	}

	@SuppressWarnings("checkstyle:FileTabCharacter")
	public CardType getCardType() {
		return cardType;
	}

	@SuppressWarnings("checkstyle:FileTabCharacter")
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

	@SuppressWarnings("checkstyle:FileTabCharacter")
	public @Override int hashCode() {
		return Objects.hash(cardType);
	}

}


