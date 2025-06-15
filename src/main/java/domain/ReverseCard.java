package domain;

public class ReverseCard extends Card {
	public ReverseCard() {
		super(CardType.REVERSE);
	}

	@Override
	public CardEffect createEffect() {
		return new ReverseEffect();
	}

	private static class ReverseEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {

		}
	}
}
