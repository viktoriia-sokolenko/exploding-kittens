package domain;

import java.util.Objects;

public class NukeCard extends Card {
	public NukeCard() { super(CardType.NUKE);  }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}
