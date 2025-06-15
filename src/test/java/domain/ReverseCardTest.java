package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReverseCardTest {
	@Test
	void createEffect_reverseCard_returnsNonNullEffect() {
		ReverseCard reverseCard = new ReverseCard();
		CardEffect effect = reverseCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_reverseEffect_reverseOrderPreservingAttackState() {
		ReverseCard reverseCard = new ReverseCard();
		CardEffect reverseEffect = reverseCard.createEffect();
		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.reverseOrderPreservingAttackState();
		EasyMock.expectLastCall();
		EasyMock.replay(mockGameContext);
		reverseEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
