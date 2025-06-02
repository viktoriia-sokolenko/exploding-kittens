package domain;

import java.util.Objects;

public class SkipCard extends Card {
	public SkipCard()  { super(CardType.SKIP); }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}

}
