package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;

public class CardEffectTests {

	private GameContext mockGameContext;
	private Player mockPlayer;

	@BeforeEach
	void setUp() {
		mockGameContext = EasyMock.createMock(GameContext.class);
		mockPlayer = EasyMock.createMock(Player.class);
	}

	@Test
	void cardEffect_canBeImplemented() {
		EasyMock.expect(mockGameContext.getCurrentPlayer()).andReturn(mockPlayer);
		EasyMock.replay(mockGameContext, mockPlayer);

		CardEffect testEffect = new CardEffect() {
			@Override
			public void execute(GameContext context) {
				Player player = context.getCurrentPlayer();
				assertSame(player, mockPlayer);
			}
		};

		assertDoesNotThrow(() -> testEffect.execute(mockGameContext));
		EasyMock.verify(mockGameContext, mockPlayer);
	}
}