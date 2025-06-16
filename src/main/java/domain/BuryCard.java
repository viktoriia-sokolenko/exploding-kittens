package domain;

public class BuryCard extends Card {
	public BuryCard() {
		super(CardType.BURY);
	}

	@Override
	public CardEffect createEffect() {
		return new BuryEffect();
	}

	private static class BuryEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			context.buryCardImplementation();
		}
	}
}
