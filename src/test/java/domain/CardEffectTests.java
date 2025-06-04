package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

public class CardEffectTests {

	private GameContext mockGameContext;

	@BeforeEach
	void setUp() {
		mockGameContext = EasyMock.createMock(GameContext.class);
	}

	@Test
	void cardEffect_canBeImplemented() {
		CardEffect testEffect = new CardEffect() {
			@Override
			public void execute(GameContext context) {
				context.getCurrentPlayer();
			}
		};

		expect(mockGameContext.getCurrentPlayer()).andReturn(null);
		replay(mockGameContext);

		assertDoesNotThrow(() -> testEffect.execute(mockGameContext));
		verify(mockGameContext);
	}
}