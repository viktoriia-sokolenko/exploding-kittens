package domain;

public class SkipCard extends Card {
	public SkipCard() {
		super(CardType.SKIP);
	}

	@Override
	public CardEffect createEffect() {
		return new SkipEffect();
	}

	private static class SkipEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			context.playCardFromCurrentPlayerHand(new SkipCard());
			context.endTurnWithoutDrawing();
		}
	}
}