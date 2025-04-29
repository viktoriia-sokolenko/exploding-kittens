package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    @Test
    public void Constructor_WithNullType_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Card(null));
    }
}
