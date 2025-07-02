package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefuseCardTest {
	@Test
	void createEffect_defuseCard_returnsNonNullEffect() {
		DefuseCard defuseCard = new DefuseCard();
		CardEffect effect = defuseCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_defuseEffect_callsHandlePlayingDefuseCard() {
		DefuseCard defuseCard = new DefuseCard();
		CardEffect defuseCardEffect = defuseCard.createEffect();

		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.handlePlayingDefuseCard();
		EasyMock.expectLastCall();
		EasyMock.replay(mockGameContext);

		defuseCardEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
