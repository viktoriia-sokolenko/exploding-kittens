package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AttackCardTests {
	private AttackCard attackCard;

	@BeforeEach
	void setUp() {
		attackCard = new AttackCard();
	}

	@Test
	void constructor_createsCardWithAttackType() {
		assertEquals(CardType.ATTACK, attackCard.getCardType());
	}

	@Test
	void createEffect_attackCard_returnsNonNullEffect() {
		CardEffect effect = attackCard.createEffect();

		assertNotNull(effect, "Effect cannot be null");
	}

	@Test
	void attackEffect_execute_callsEndTurnWithoutDrawingForAttacks() {
		GameContext gameContext = EasyMock.createMock(GameContext.class);
		CardEffect cardEffect = attackCard.createEffect();

		gameContext.endTurnWithoutDrawingForAttacks();
		EasyMock.expectLastCall().once();
		EasyMock.replay(gameContext);

		cardEffect.execute(gameContext);
		EasyMock.verify(gameContext);
	}
}
