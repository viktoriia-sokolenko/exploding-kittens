package domain;

public class SwapTopAndBottomCard extends Card {
	public SwapTopAndBottomCard() {
		super(CardType.SWAP_TOP_AND_BOTTOM);
	}

	@Override
	public CardEffect createEffect() {
		return new SwapTopAndBottomCard.SwapTopAndBottomEffect();
	}

	private static class SwapTopAndBottomEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			context.swapTopAndBottomDeckCards();
		}
	}
}
