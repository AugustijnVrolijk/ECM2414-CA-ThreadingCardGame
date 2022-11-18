import java.util.ArrayList;

public class CardDeck {

    private int deckId;
    private ArrayList<Card> cardList = new ArrayList<>();

    CardDeck(int deckId){
        this.deckId = deckId;
    }

    public void addCard(Card card) {
        cardList.add(card);
    }
    public Card removeCard(Card card) {
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
