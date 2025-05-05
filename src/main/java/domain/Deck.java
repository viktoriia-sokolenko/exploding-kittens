package domain;

import java.util.*;

public class Deck {
	private final Random rand;
	private final List<Card> deck;

	public Deck(List<Card> cardList, Random rand) {
		Objects.requireNonNull(cardList, "List of Cards cannot be null");
		this.deck = new ArrayList<>(cardList);
		this.rand = rand;
	}

	public Card peekTop() {
		if (deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}
		return this.deck.get(deck.size() - 1);
	}

	public Card draw() {
		if (this.deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}

		return this.deck.remove(this.deck.size() - 1);
	}

	public void insertCardAt(Card card, int index) {
		Objects.requireNonNull(card, "Card type cannot be null");
		if (isIndexOutOfBounds(index)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		this.deck.add(index, card);

	}

	public int getDeckSize() {
		return this.deck.size();
	}

	public void shuffleDeck() {
		for (int deckIndex = deck.size() - 1; deckIndex > 0; deckIndex--) {
			int indexToSwap = rand.nextInt(deckIndex + 1);
			Card temporaryCard = deck.get(indexToSwap);
			deck.set(indexToSwap, deck.get(deckIndex));
			deck.set(deckIndex, temporaryCard);
		}

	}

	private boolean isIndexOutOfBounds(int index) {
		return index < 0 || index > this.deck.size();
	}
}
