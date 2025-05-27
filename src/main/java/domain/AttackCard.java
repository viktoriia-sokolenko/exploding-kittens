package domain;
import java.util.Objects;

public class AttackCard extends Card {
	public AttackCard() { super(CardType.ATTACK); }

	@Override
	public void play(Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
	}
}
