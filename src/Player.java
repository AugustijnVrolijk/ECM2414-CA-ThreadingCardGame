import java.util.ArrayList;

public class Player {
    public int PlayerID;
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

}
