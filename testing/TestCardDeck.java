import org.junit.Before;
import org.junit.Test;

public class TestCardDeck {
    CardDeck deck;
    Card card;

    @Before
    public void setUp(){
        deck = new CardDeck(1);
        card = new Card(1);
    }

    @Test
    public void testAddCard(){
        deck.addCard(card);
        assert (deck.getCardList().size() == 1);
    }

    @Test
    public void testRemoveCard(){
        deck.addCard(card);
        deck.removeCard(card);
        assert (deck.getCardList().size() == 0);
    }
}
