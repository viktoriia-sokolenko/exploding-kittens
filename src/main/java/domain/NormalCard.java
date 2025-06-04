package domain;

//import java.util.Objects;

public class NormalCard	 extends Card {
	public NormalCard() { super(CardType.NORMAL); }

	@Override
	public CardEffect createEffect() {
		// Temporary empty implementation
		return null;
	}
}
