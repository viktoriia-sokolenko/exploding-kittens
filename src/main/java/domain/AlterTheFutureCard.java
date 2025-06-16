package domain;

public class AlterTheFutureCard extends Card {

	public AlterTheFutureCard() {
		super(CardType.ALTER_THE_FUTURE);
	}

	@Override
	public CardEffect createEffect() {
		return new AlterTheFutureEffect();
	}

	private static class AlterTheFutureEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			return;
		}
	}
}
