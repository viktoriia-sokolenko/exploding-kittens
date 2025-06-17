package domain;

public class NormalCard	 extends Card {
	public NormalCard() { super(CardType.NORMAL); }

	@Override
	public CardEffect createEffect() {
		return null;
	}
}
