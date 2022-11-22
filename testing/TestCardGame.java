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

    @Test
    public void checkCardsDealt() throws IOException {
        game.readPack("pack.txt");
        game.initialisePlayersAndDecks(4);
        game.dealCards();

        for (Player player: game.players){
            assert (player.getCards().size() == 4);
        }

        for (CardDeck deck: game.decks){
            assert (deck.getCardList().size() == 4);
        }
    }

}
