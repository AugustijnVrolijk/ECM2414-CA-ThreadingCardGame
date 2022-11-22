import org.junit.Before;
import org.junit.Test;

public class TestPlayer {

    private Player player;
    private Card card;
    private Card card1;
    private Card card2;
    private Card card3;

    @Before
    public void setUp() {
        player = new Player(0);
        card = new Card(1);
        card1 = new Card(1);
        card2 = new Card(1);
        card3 = new Card(1);
    }

    @Test
    public void testAppendToOutputFile() {
        // TODO
    }

    @Test
    public void testDrawCard() {
        // player.addCard(card);
        // assert (player.getCards().size()==1);
    }

    @Test
    public void testDiscardCard() {
        // player.discardCard(card,0);
        // assert (player.getCards().size()==0);
    }

    @Test
    public void testCheckHand() {
        player.addCard(card);
        player.addCard(card1);
        player.addCard(card2);
        player.addCard(card3);
        assert player.checkHand();
    }
}
