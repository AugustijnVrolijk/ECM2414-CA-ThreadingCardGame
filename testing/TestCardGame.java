import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class TestCardGame {

    private CardGame game;

    @Before
    public void setUp() {
        game = new CardGame("test");
    }

    @Test
    public void testCheckPack() throws IOException {
        assert game.checkPack(4,"pack.txt");
    }
}
