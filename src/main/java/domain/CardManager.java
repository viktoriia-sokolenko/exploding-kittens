    package domain;

    import java.util.Objects;

    public class CardManager {

        public void playCard(Card card, Player player) {
            Objects.requireNonNull(card, "Card cannot be null");
            Objects.requireNonNull(player, "Player cannot be null");
        }
    }
