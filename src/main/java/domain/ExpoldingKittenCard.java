package domain;

import java.util.Objects;

public class ExpoldingKittenCard extends Card {
	public ExpoldingKittenCard() {
		super(CardType.EXPLODING_KITTEN);
	}

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}