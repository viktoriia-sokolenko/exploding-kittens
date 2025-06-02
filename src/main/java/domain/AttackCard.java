package domain;

public class AttackCard extends Card {
	public AttackCard() { super(CardType.ATTACK); }
	@Override
	public CardEffect createEffect() {
		return null;
	}
}
