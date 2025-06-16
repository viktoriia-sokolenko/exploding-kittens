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
		checkIndicesList(newIndices);

		List<Card> rearrangedTopThreeCards = new ArrayList<>();
		for (int index : newIndices) {
			checkIndex(index);
			rearrangedTopThreeCards.add(this.deck.get(index));
		}

		int topIndex = this.deck.size() - 1;
		for (int i = 0; i < newIndices.size(); i++) {
			this.deck.set(topIndex - i, rearrangedTopThreeCards.get(i));
		}
	}

	public void swapTopAndBottom() {
		if (deck.isEmpty()) {
			throw new NoSuchElementException("Deck is empty");
		}

		int topIndex = deck.size() - 1;
		Card topCard = deck.get(topIndex);

		int bottomIndex = 0;
		Card bottomCard = deck.get(bottomIndex);

		deck.set(topIndex, bottomCard);
		deck.set(bottomIndex, topCard);
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

	private void checkIndicesList(List<Integer> indicesList) {
		if (areTooManyIndices(indicesList)) {
			throw new IllegalArgumentException(
					"Number of indices is larger than the deck size");
		}

		if (areIndicesDuplicate(indicesList)) {
			throw new IllegalArgumentException("Duplicate indices are not allowed");
		}
	}

	private Boolean areTooManyIndices(List<Integer> indices) {
		return indices.size() > this.deck.size();
	}

	private Boolean areIndicesDuplicate(List<Integer> indices) {
		Set<Integer> uniqueIndices = new HashSet<>(indices);
		return (uniqueIndices.size() < indices.size());
	}

	private void checkIndex(int index) {
		if (isIndexNotAllowed(index)) {
			throw new IllegalArgumentException(
					"With deck size s, indices must be [s - 1, s - 3]");
		}
		if (index < 0) {
			throw new IllegalArgumentException(
					"Negative indices are not allowed");
		}
	}

	private Boolean isIndexNotAllowed(int index) {
		int indexForTopCard = this.deck.size() - 1;
		int indexForSecondCardFromTop = this.deck.size() - 2;
		final int cardsToRearrange = 3;
		int indexForThirdCardFromTop = this.deck.size() - cardsToRearrange;
		List<Integer> allowedIndices = new ArrayList<>(
				List.of(indexForTopCard,
						indexForSecondCardFromTop,
						indexForThirdCardFromTop));
		return !allowedIndices.contains(index);
	}
}
