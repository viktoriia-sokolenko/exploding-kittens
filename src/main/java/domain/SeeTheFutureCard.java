package domain;

public class SeeTheFutureCard extends Card {
	public SeeTheFutureCard() {
		super(CardType.SEE_THE_FUTURE);
	}

	@Override
	public CardEffect createEffect() {
		return new SeeTheFutureEffect();
	}

	private static class SeeTheFutureEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			context.viewTopTwoCardsFromDeck();
		}
	}
}
