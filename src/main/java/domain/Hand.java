package domain;

import java.util.HashMap;
import java.util.Map;

public class Hand {
	private final Map<CardType, Integer> cards;

	public Hand() {
		this.cards = new HashMap<>();
	}

	public boolean isEmpty() {
		return this.cards.isEmpty();
	}

	public void addCard(Card card) {
		CardType type = card.getCardType();
		if (this.cards.containsKey(type)) {
			this.cards.put(type, this.cards.get(type) + 1);
		}
		else {
			this.cards.put(type, 1);
		}
	}

	public boolean containsCardType(CardType cardType) {
		return this.cards.containsKey(cardType);
	}

	public int getNumberOfCards() {
		int totalNumberOfCards = 0;
		for (int count : this.cards.values()) {
			totalNumberOfCards += count;
		}
		return totalNumberOfCards;
	}
}