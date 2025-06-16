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

}
