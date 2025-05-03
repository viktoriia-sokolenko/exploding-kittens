package domain;

public class CardFactory {
	public void createCard(CardType cardType) {
		if (cardType == null) {
			throw new NullPointerException();
		}
	}
}