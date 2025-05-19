package domain;

import java.util.Objects;

public class Player {
	Hand hand;

	public Player(Hand hand) {
		this.hand = hand;
	}

	public Integer getCardTypeCount(CardType cardType) {
		return hand.getCountOfCardType(cardType);
	}
}
