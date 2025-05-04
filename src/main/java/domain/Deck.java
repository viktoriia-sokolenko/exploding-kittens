package domain;

import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
	private List<Card> deck;

	public Deck(List<Card> cardList) {
		this.deck = cardList;
	}

	public Card draw() {
		if (this.deck.isEmpty()){
			throw new NoSuchElementException("Deck is empty");
		}

		return this.deck.remove(this.deck.size() - 1);
	}
}
