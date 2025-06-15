package domain;

import java.util.*;

public class Deck {
	private final List<Card> deck;

	public Deck(List<Card> cardList) {
		Objects.requireNonNull(cardList, "List of Cards cannot be null");
		this.deck = new ArrayList<>(cardList);
	}

	public Card getCardAt(int index) {
		if (isIndexOutOfBounds(index) || isIndexEqualToDeckSize(index)) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return this.deck.get(index);
	}

	public Card peekTop() {
		if (deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}
		return this.deck.get(deck.size() - 1);
	}

	public List<Card> peekTopTwoCards() {
		return peekTopCards(2);
	}

	public List<Card> peekTopThreeCards() {
		final int numberToLookUp = 3;
		return peekTopCards(numberToLookUp);
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

	public void shuffleDeck(Random rand) {
		Objects.requireNonNull(rand, "Random cannot be null");
		//Fischer Yates Algorithm
		for (int deckIndex = deck.size() - 1; deckIndex > 0; deckIndex--) {
			int indexToSwap = rand.nextInt(deckIndex + 1);
			Card temporaryCard = deck.get(indexToSwap);
			deck.set(indexToSwap, deck.get(deckIndex));
			deck.set(deckIndex, temporaryCard);
		}
	}

	public void rearrangeTopThreeCards(List<Integer> newIndices) {
		if (this.deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}
		if (newIndices.size() > this.deck.size()) {
			throw new IllegalArgumentException(
					"Number of indices is larger than the deck size");
		}
		throw new IllegalArgumentException(
				"Negative indices are not allowed");
	}

	private boolean isIndexOutOfBounds(int index) {
		return index < 0 || index > this.deck.size();
	}

	private boolean isIndexEqualToDeckSize(int index) {
		return this.deck.size() == index;
	}

	private List<Card> peekTopCards (int count) {
		if (deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}
		ArrayList<Card> cardList = new ArrayList<>();
		int cardCount = 1;
		int cardsToAdd = count;
		while (cardsToAdd > 0) {
			if (this.deck.size() < cardCount) {
				return cardList;
			}
			cardList.add(this.deck.get(deck.size() - cardCount));
			cardCount++;
			cardsToAdd--;
		}
		return cardList;
	}
}
