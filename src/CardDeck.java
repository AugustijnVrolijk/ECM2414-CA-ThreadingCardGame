import java.io.*;
import java.util.ArrayList;

public class CardDeck {

    private int deckId;
    private File outputFile;
    private ArrayList<Card> cardList = new ArrayList<>();

    CardDeck(int deckId){
        this.deckId = deckId;
        String basePath = new File("").getAbsolutePath();
        outputFile = new File(String.format("%s/outputTextFiles/deck%d_output.txt",basePath, this.deckId)); //create output file for this deck
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
    public int getDeckId() {
        return deckId;
    }

    public void recordFinalHand() {
        try (FileWriter f = new FileWriter(outputFile, false);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b)) {
            p.println(String.format("Deck %d contents: %s",deckId,cardList.toString()));} //write final cards in deck to output file
        catch (IOException i) { i.printStackTrace(); }
    }
}
