package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SkipCardTests {
	private SkipCard skipCard;

	@BeforeEach
	void setUp() {
		skipCard = new SkipCard();
	}

	@Test
	void constructor_createsCardWithSkipType() {
		assertEquals(CardType.SKIP, skipCard.getCardType());
	}

	@Test
	void createEffect_skipCard_returnsNonNullEffect() {
		CardEffect effect = skipCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void execute_skipEffect_callsEndTurnWithoutDrawing() {
		SkipCard skipCard = new SkipCard();
		CardEffect skipEffect = skipCard.createEffect();
		GameContext mockGameContext = EasyMock.createMock(GameContext.class);
		mockGameContext.endTurnWithoutDrawing();
		EasyMock.expectLastCall();
		EasyMock.replay(mockGameContext);
		skipEffect.execute(mockGameContext);
		EasyMock.verify(mockGameContext);
	}
}
