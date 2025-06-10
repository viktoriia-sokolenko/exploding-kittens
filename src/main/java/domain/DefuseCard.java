package domain;

//import java.util.Objects;

public class DefuseCard extends Card {
	public DefuseCard() {
		super(CardType.DEFUSE);
	}

	@Override
	public CardEffect createEffect() {
		// Temporary empty implementation
		return null;
	}
}