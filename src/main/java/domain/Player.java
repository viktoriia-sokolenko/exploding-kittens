package domain;

public class Player {
	Hand hand;
	Boolean activeStatus = true;

	public Player(Hand hand) {
		this.hand = hand;
	}

	public Integer getCardTypeCount(CardType cardType) {
		return hand.getCountOfCardType(cardType);
	}

	public void drawCard(Deck deck) {
		Card drawnCard = deck.draw();
		if (drawnCard.getCardType() == CardType.EXPLODING_KITTEN) {
			handleExplodingKitten();
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

	private void handleExplodingKitten() {
		if (this.hand.containsCardType(CardType.DEFUSE)) {
			this.removeDefuseCard();
		}
		else {
			this.activeStatus = false;
		}
	}

	private void removeDefuseCard() {
		Card defuseCard = new Card (CardType.DEFUSE);
		this.hand.removeCard(defuseCard);
	}
}
