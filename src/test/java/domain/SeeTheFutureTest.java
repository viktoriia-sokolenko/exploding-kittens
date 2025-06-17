package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SeeTheFutureTest {
	@Test
	void createEffect_seeTheFutureCard_returnsNonNullEffect() {
		SeeTheFutureCard seeTheFutureCard = new SeeTheFutureCard();
		CardEffect effect = seeTheFutureCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_seeTheFutureEffect_callsViewTopTwoCardsFromDeck() {
		SeeTheFutureCard seeTheFutureCard = new SeeTheFutureCard();
		CardEffect seeTheFutureEffect = seeTheFutureCard.createEffect();

		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.viewTopTwoCardsFromDeck();
		EasyMock.expectLastCall().once();
		EasyMock.replay(mockGameContext);

		seeTheFutureEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
