package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

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
		List<Card> mockCards = List.of(EasyMock.createMock(Card.class),
				EasyMock.createMock(Card.class));;
		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		EasyMock.expect(mockGameContext.viewTopTwoCardsFromDeck())
				.andReturn(mockCards).once();
		EasyMock.replay(mockGameContext);
		seeTheFutureEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
