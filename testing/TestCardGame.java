import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class TestCardGame {

    private CardGame game;
    String basePath = (new File("")).getAbsolutePath();

    @Before
    public void setUp() {
        game = new CardGame("test");
    }

    @Test
    public void testCheckPack() throws IOException {
        assert game.checkPack(4,"pack.txt");
    }

    @Test
    public void testReadPack() throws IOException {
        game.readPack("pack.txt");
    }

    @Before
    public void setUp2() throws IOException {
        game.readPack("pack2.txt");
        game.initialisePlayersAndDecks(4);
        game.dealCards();
        game.setUpTopology(4);
    }

    @Test
    public void testInitialisePlayersAndDecks(){
        assert (game.players.size() == 4);
        assert (game.decks.size() == 4);
    }

    @Test
    public void testDealCards() {

        for (Player player: game.players){
            assert (player.getCards().size() == 4);
        }

        for (CardDeck deck: game.decks){
            assert (deck.getCardList().size() == 4);
        }
    }

    @Test
    public void testSetUpTopology(){
        for (Player player : game.players){
            assert (player.getPlayerId()==player.getDeckAfter().getDeckId());
        }
    }

}
