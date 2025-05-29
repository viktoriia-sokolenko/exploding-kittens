package domain;

import java.util.Objects;

public class ShuffleCard  extends Card {
	public ShuffleCard() { super(CardType.SHUFFLE); }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}
