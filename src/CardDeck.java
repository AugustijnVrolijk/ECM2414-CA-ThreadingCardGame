import java.util.ArrayList;

public class CardDeck {

    private int deckId;
    private ArrayList<Card> cardList = new ArrayList<>();

    CardDeck(int deckId){
        this.deckId = deckId;
    }

    public synchronized void addCard(Card card) {
        cardList.add(card);
    }
    public synchronized Card removeCard(Card card) {
        cardList.remove(card);
        return card;
    }

    public synchronized ArrayList<Card> getCardList() {
        return cardList;
    }
    public synchronized int getDeckId() {
        return deckId;
    }
}
