package domain;

import java.util.Objects;

public class FavorCard extends Card {
	public FavorCard() { super(CardType.FAVOR); }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}
