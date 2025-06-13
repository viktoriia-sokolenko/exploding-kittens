package domain;

import java.util.*;

public class Hand {
	private final Map<CardType, Integer> cards;

	public Hand() {
		this.cards = new HashMap<>();
	}

	public boolean isEmpty() {
		return this.cards.isEmpty();
	}

	public void addCard(Card card) {
		Objects.requireNonNull(card, "Card cannot be null");
		CardType type = card.getCardType();
		if (type == CardType.EXPLODING_KITTEN) {
			throw new IllegalArgumentException(
					"Exploding Kitten should not be added to Hand");
		}
		this.cards.put(type, this.cards.getOrDefault(type, 0) + 1);
	}

	public boolean containsCardType(CardType cardType) {
		Objects.requireNonNull(cardType, "CardType cannot be null");
		return this.cards.containsKey(cardType);
	}

	public int getNumberOfCards() {
		int totalNumberOfCards = 0;
		for (int count : this.cards.values()) {
			totalNumberOfCards += count;
		}
		return totalNumberOfCards;
	}

	public void removeCard(Card card) {
		Objects.requireNonNull(card, "Card cannot be null");

		if (this.isEmpty()) {
			throw new IllegalStateException("Hand empty: can not remove card");
		}

		CardType cardType = card.getCardType();
		Integer count = this.cards.get(cardType);
		if (!isValidCount(count)) {
			throw new IllegalArgumentException("Card not in hand: can not remove card");
		}
		if (count == 1) {
			this.cards.remove(cardType);
		}
		else {
			this.cards.put(cardType, count - 1);
		}
	}

	public void removeDefuseCard() {
		Card defuseCard = new DefuseCard();
		this.removeCard(defuseCard);
	}

	public int getCountOfCardType(CardType cardType) {
		Objects.requireNonNull(cardType, "CardType cannot be null");
		return this.cards.getOrDefault(cardType, 0);
	}

	private boolean isValidCount (Integer count) {
		return count != null;
	}

	private Card createCardByType(CardType cardType) {
		switch (cardType) {
			case SKIP:
				return new SkipCard();
			case ATTACK:
				return new AttackCard();
			case FAVOR:
				return new FavorCard();
			case DEFUSE:
				return new DefuseCard();
			case SEE_THE_FUTURE:
				return new SeeTheFutureCard();
			case SHUFFLE:
				return new ShuffleCard();
			case ALTER_THE_FUTURE:
				return new AlterTheFutureCard();
			case NORMAL:
				return new NormalCard();
			case NUKE:
				return new NukeCard();
			default:
				throw new IllegalArgumentException("Unknown card type: "
						+ cardType);
		}
	}

	public List<CardType> getAvailableCardTypes() {
		List<CardType> availableTypes = new ArrayList<>();
		for (CardType type : CardType.values()) {
			if (getCountOfCardType(type) > 0) {
				availableTypes.add(type);
			}
		}
		return availableTypes;
	}

	public CardType parseCardType(String input) {
		if (input == null || input.trim().isEmpty()) {
			return null;
		}

		String normalizedCardType = input
				.trim().toUpperCase()
				.replace(" ", "_");

		try {
			CardType cardType = CardType.valueOf(normalizedCardType);
			return containsCardType(cardType) ? cardType : null;
		} catch (IllegalArgumentException error) {
			for (CardType cardType : getAvailableCardTypes()) {
				if (cardType.name().startsWith(normalizedCardType) ||
						cardType.name().contains(normalizedCardType)) {
					return cardType;
				}
			}
		}
		return null;
	}
}