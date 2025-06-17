package domain;

public class FavorCard extends Card {
	public FavorCard() { super(CardType.FAVOR); }

	@Override
	public CardEffect createEffect() {
		return new FavorCard.FavorEffect();
	}

	private static class FavorEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			context.transferCardBetweenPlayers();
		}
	}
}
