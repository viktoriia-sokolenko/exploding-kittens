package domain;

public class ExpoldingKittenCard extends Card {
	public ExpoldingKittenCard() {
		super(CardType.EXPLODING_KITTEN);
	}

	@Override
	public CardEffect createEffect() {
		// Temporary empty implementation
		return null;
	}
}