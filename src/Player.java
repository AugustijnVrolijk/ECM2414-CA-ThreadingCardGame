import java.util.ArrayList;
import java.io.*;
public class Player {
    private int playerId;
    private ArrayList<Card> cards = new ArrayList<>();
    private File outputFile;

    Player(int playerId) {
        this.playerId = playerId;
        if(new File("../player\"+String.valueOf(this.playerId)+\"_output.txt").isFile()) {

        }
        else {
            outputFile = new File("../player" + String.valueOf(this.playerId) + "_output.txt");
        }
    }
    private void appendToOutputFile(String text, boolean append) {
        try (FileWriter f = new FileWriter(outputFile, append);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) {
            p.println(text); }
        catch (IOException i) { i.printStackTrace(); }
    }
    public void drawCard(Card card){
        cards.add(card);
        appendToOutputFile(String.format("player %d draws a %d from deck %d",this.playerId, card.getCardNumber(), 1), true); //replace 1 with the card deck id
    }
    public Card discardCard(Card card) {
        cards.remove(card);
        appendToOutputFile(String.format("player %d discards a %d to deck %d",this.playerId, card.getCardNumber(), 1), true); //replace 1 with the card deck id
        return card;
    }
    public boolean checkHand() {
        for (int i = 0; i < cards.size()-1; i++) {
            if (cards.get(i).getCardNumber() != cards.get(i+1).getCardNumber()){
                return false;
            }
        }
        appendToOutputFile(String.format("player %d wins",this.playerId), true);
        return true;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
