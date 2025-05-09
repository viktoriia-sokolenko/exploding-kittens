package domain;

import java.util.ArrayList;
import java.util.List;

public class Hand {
	private List<Card> cards;

	public Hand() {
		cards = new ArrayList<Card>();
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public void addCard(Card card) {
		cards.add(card);
	}
}