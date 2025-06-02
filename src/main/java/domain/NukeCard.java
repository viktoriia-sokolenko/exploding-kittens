package domain;

//import java.util.Objects;

public class NukeCard extends Card {
	public NukeCard() { super(CardType.NUKE);  }

	@Override
	public CardEffect createEffect() {
		// Temporary empty implementation
		return null;
	}
}
