package domain;

//import java.util.Objects;

public class FavorCard extends Card {
	public FavorCard() { super(CardType.FAVOR); }

	@Override
	public CardEffect createEffect() {
		// Temporary empty implementation
		return null;
	}
}
