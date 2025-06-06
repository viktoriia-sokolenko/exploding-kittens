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
			context.endTurnWithoutDrawingForAttacks();
		}
	}
}