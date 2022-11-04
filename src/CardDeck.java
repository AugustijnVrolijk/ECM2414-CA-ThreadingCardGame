import java.util.ArrayList;

public class CardDeck {

    private static int id = 0;
    public int deckId;
    private ArrayList<Card> cardList = new ArrayList<>();

    CardDeck(){
        this.deckId = id++;
    }

    private void addCard(Card card) {
        cardList.add(card);
    }
    private Card removeCard(Card card) {
        cardList.remove(card);
        return card;
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }
    public int getDeckId() {
        return deckId;
    }
}
