import java.util.ArrayList;

public class Player {
    private int PlayerID;
    private ArrayList<Card> Cards = new ArrayList<>();

    Player(int PlayerID) {
        this.PlayerID = PlayerID;
    }

    public void AddCard(Card card){
        this.Cards.add(card);
    }
    public Card RemoveCard(Card card) {
        this.Cards.remove(card);
        return card;
    }
    public boolean CheckHand() {
        int x = 0;
        for (int i = 0; i < Cards.size()-1; i++) {
            if (Cards.get(i).getCardNumber() != Cards.get(i+1).getCardNumber()){
                return false;
            }
        }
        return true;
    }

}
