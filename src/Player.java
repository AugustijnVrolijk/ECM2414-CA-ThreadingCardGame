import java.util.ArrayList;

public class Player {
    private int playerId;
    private ArrayList<Card> cards = new ArrayList<>();

    Player(int playerId) {
        this.playerId = playerId;
    }

    public void addCard(Card card){
        this.cards.add(card);
    }
    public Card removeCard(Card card) {
        this.cards.remove(card);
        return card;
    }
    public boolean checkHand() {
        int x = 0;
        for (int i = 0; i < cards.size()-1; i++) {
            if (cards.get(i).getCardNumber() != cards.get(i+1).getCardNumber()){
                return false;
            }
        }
        return true;
    }
}
