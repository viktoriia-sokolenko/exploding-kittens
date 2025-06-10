package domain;

public class ShuffleCard extends Card {
	public ShuffleCard() {
		super(CardType.SHUFFLE);
	}

	@Override
	public CardEffect createEffect() {
		return new ShuffleEffect();
	}

	private static class ShuffleEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			context.shuffleDeckFromDeck();
		}
	}
}
