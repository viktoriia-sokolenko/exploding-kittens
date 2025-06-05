package domain;

//import java.util.Objects;

public class ShuffleCard  extends Card {
	public ShuffleCard() { super(CardType.SHUFFLE); }

	@Override
	public CardEffect createEffect() {
		// Temporary empty implementation
		return null;
	}
}
