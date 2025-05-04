package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Deck {
	private List<Card> deck;

	public Deck(List<Card> cardList) {
		Objects.requireNonNull(cardList, "List of Cards cannot be null");
		this.deck = new ArrayList<>(cardList);
	}

	public Card draw() {
		if (this.deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}

		return this.deck.remove(this.deck.size() - 1);
	}

	public void insertAt(int index, Card card) {
		Objects.requireNonNull(card, "Card type cannot be null");
		if (index < 0 || index > this.deck.size()) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}

	}

	public int getDeckSize(){
		return this.deck.size();
	}
}
