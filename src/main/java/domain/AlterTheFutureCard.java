package domain;

import java.util.Objects;

public class AlterTheFutureCard extends Card {
	public AlterTheFutureCard() { super(CardType.ALTER_THE_FUTURE); }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}
