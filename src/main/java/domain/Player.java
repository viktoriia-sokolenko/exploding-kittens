package domain;

public class Player {
	Hand hand;
	boolean activeStatus = true;

	Player(Hand hand) {
		this.hand = hand;
	}

	public Integer getCardTypeCount(CardType cardType) {
		return hand.getCountOfCardType(cardType);
	}

	public void drawCard(Deck deck) {
		Card drawnCard = deck.draw();

		if (drawnCard.getCardType() == CardType.EXPLODING_KITTEN) {
			handleExplodingKitten(deck, drawnCard);
		}
		else {
			hand.addCard(drawnCard);
		}
	}

	public void playCard(Card card) {
		hand.removeCard(card);
	}

	public boolean isInGame() {
		return activeStatus;
	}

	private void handleExplodingKitten(Deck deck, Card explodingKittenCard) {
		if (this.hand.containsCardType(CardType.DEFUSE)) {
			this.hand.removeDefuseCard();
			deck.insertCardAt(explodingKittenCard, 1);
		}
		else {
			this.activeStatus = false;
		}
	}
}
