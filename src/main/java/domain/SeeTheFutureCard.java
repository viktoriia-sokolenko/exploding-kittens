package domain;

import java.util.Objects;

public class SeeTheFutureCard extends Card {
	public SeeTheFutureCard() { super(CardType.SEE_THE_FUTURE); }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}
