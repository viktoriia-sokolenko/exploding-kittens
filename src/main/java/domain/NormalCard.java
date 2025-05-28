package domain;

import java.util.Objects;

public class NormalCard  extends Card {
	public NormalCard() { super(CardType.NORMAL); }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}
