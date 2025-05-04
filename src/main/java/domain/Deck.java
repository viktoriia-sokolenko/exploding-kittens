package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
	private List<Card> deck;

	public Deck(List<Card> cardList) {
		this.deck = new ArrayList<>(cardList);
	}

	public Card draw() {
		if (this.deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}

		return this.deck.remove(this.deck.size() - 1);
	}

	public void insertAt(int index, Card card) {
		if (index < 0 || index > this.deck.size() || card == null) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}

	}

	public int getDeckSize(){
		return this.deck.size();
	}
}
