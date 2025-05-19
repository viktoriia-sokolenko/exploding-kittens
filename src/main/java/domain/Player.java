package domain;

public class Player {
	Hand hand;

	public Player(Hand hand) {
		this.hand = hand;
	}

	public Integer getCardTypeCount(CardType cardType) {
		return hand.getCountOfCardType(cardType);
	}

	public void drawCard(Deck deck) {
		Card drawnCard = deck.draw();
		if (drawnCard.getCardType() == CardType.EXPLODING_KITTEN) {
			if (this.hand.containsCardType(CardType.DEFUSE)) {
				this.removeDefuseCard();
			}
		}
		else {
			hand.addCard(drawnCard);
		}
	}

	private void removeDefuseCard() {
		Card defuseCard = new Card (CardType.DEFUSE);
		this.hand.removeCard(defuseCard);
	}
}
