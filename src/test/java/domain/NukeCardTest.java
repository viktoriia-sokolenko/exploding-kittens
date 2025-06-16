package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NukeCardTest {

    @Test
    void createEffect_nukeCard_returnsNonNullEffect() {
        NukeCard nukeCard = new NukeCard();
        CardEffect effect = nukeCard.createEffect();

        assertNotNull(effect, "Effect cannot be null");
    }

    @Test
    void execute_nukeEffect_callsMoveAllExplodingKittensToTop() {
        NukeCard nukeCard = new NukeCard();
        CardEffect nukeEffect = nukeCard.createEffect();
        GameContext mockGameContext = EasyMock.createMock(GameContext.class);

        mockGameContext.moveAllExplodingKittensToTop();
        EasyMock.expectLastCall();
        EasyMock.replay(mockGameContext);

        nukeEffect.execute(mockGameContext);

        EasyMock.verify(mockGameContext);
    }

}
