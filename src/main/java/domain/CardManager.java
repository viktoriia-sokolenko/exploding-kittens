	package domain;

	import java.util.Objects;

	public class CardManager {

		public void playCard(Card card, Player player) {
			Objects.requireNonNull(card, "Card cannot be null");
			Objects.requireNonNull(player, "Player cannot be null");

			CardType type = card.getCardType();
			int count = player.getCardTypeCount(type);

			if (count <= 0) {
				throw new IllegalArgumentException("Player does not have this card type");
			}

			GameContext context = new GameContext(player);
			CardEffect effect = card.createEffect();
			effect.execute(context);
		}
	}
