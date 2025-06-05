package domain;

public class AttackCard extends Card {

	public AttackCard() {
		super(CardType.ATTACK);
	}

	@Override
	public CardEffect createEffect() {
		return new AttackEffect();
	}

	private static class AttackEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			// End Turn without drawing
			// Next Player Take Two Turns in a Row
		}
	}
}