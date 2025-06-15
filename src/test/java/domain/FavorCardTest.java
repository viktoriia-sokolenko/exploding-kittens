package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FavorCardTest {
	@Test
	void createEffect_favorCard_returnsNonNullEffect() {
		FavorCard favorCard = new FavorCard();
		CardEffect effect = favorCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_favorEffect_callsTransferCardBetweenPlayers() {
		FavorCard favorCard = new FavorCard();
		CardEffect favorEffect = favorCard.createEffect();
		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.transferCardBetweenPlayers();
		EasyMock.expectLastCall();
		EasyMock.replay(mockGameContext);

		favorEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
