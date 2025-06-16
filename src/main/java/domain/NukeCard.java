package domain;

public class NukeCard extends Card {
	public NukeCard() {
		super(CardType.NUKE);
	}

	@Override
	public CardEffect createEffect() {
		return new NukeEffect();
	}

	private static class NukeEffect implements CardEffect {
		@Override
		public void execute(GameContext context) {
			return;
		}
	}
}