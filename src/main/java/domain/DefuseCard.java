package domain;

public class DefuseCard extends Card {
	public DefuseCard() {
		super(CardType.DEFUSE);
	}

	@Override
	public CardEffect createEffect() {
		return new DefuseEffect();
	}

	private static class DefuseEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			context.handlePlayingDefuseCard();
		}
	}
}