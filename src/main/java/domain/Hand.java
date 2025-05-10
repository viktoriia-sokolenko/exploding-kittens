package domain;

import java.util.HashMap;
import java.util.Map;

public class Hand {
	private final Map<CardType, Integer> cards;

	public Hand() {
		cards = new HashMap<>();
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public void addCard(Card card) {
		CardType type = card.getCardType();
		if (cards.containsKey(type)) {
			cards.put(type, cards.get(type) + 1);
		}
		else {
			cards.put(type, 1);
		}
	}

	public boolean containsCardType(CardType cardType) {
		return cards.containsKey(cardType);
	}

	public int getNumberOfCards() {
		return cards.size();
	}
}