package domain;

import java.util.Objects;

public class DefuseCard extends Card {
	public DefuseCard() {
		super(CardType.DEFUSE);
	}

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}