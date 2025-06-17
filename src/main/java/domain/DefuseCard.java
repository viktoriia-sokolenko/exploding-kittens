package domain;

public class DefuseCard extends Card {
	public DefuseCard() {
		super(CardType.DEFUSE);
	}

	@Override
	public CardEffect createEffect() {
		return null;
	}
}