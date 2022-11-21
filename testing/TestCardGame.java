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

        System.out.println(game.decks.size());


        for (CardDeck deck: game.decks){
            System.out.println("Deck: "+deck.getDeckId());
            for (Card card: deck.getCardList()){
                System.out.println(card.getCardNumber());
            }
        }
    }

}
