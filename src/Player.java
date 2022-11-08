import java.util.ArrayList;
import java.io.*;
public class Player {
    private int playerId;
    private ArrayList<Card> cards = new ArrayList<>();
    private File outputFile;

    Player(int playerId) {
        this.playerId = playerId;
        this.outputFile = new File("../player"+String.valueOf(this.playerId)+"_output.txt");
    }

    public void addCard(Card card){
        cards.add(card);
    }
    public Card removeCard(Card card) {
        cards.remove(card);
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
