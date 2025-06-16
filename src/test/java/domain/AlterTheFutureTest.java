package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlterTheFutureTest {

	@Test
	void createEffect_alterTheFutureCard_returnsNonNullEffect() {
		AlterTheFutureCard alterTheFutureCard = new AlterTheFutureCard();
		CardEffect effect = alterTheFutureCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_alterTheFutureEffect_callsRearrangeTopThreeCardsFromDeck() {
		AlterTheFutureCard alterTheFutureCard = new AlterTheFutureCard();
		CardEffect favorEffect = alterTheFutureCard.createEffect();
		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.rearrangeTopThreeCardsFromDeck();
		EasyMock.expectLastCall();
		EasyMock.replay(mockGameContext);

		favorEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
