	package domain;

	import java.util.Objects;

	public class CardManager {

		public void playCard(Card card, Player player, GameContext gameContext) {
			Objects.requireNonNull(card, "Card cannot be null");
			Objects.requireNonNull(player, "Player cannot be null");

			player.removeCardFromHand(card.getCardType());

			CardEffect effect = card.createEffect();
			effect.execute(gameContext);
		}
	}
