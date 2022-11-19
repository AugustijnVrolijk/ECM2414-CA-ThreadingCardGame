import java.util.ArrayList;
import java.io.*;
public class Player extends Thread {
    private int playerId;
    private ArrayList<Card> cards = new ArrayList<>();
    private File outputFile;
    private CardDeck deckBefore;
    private CardDeck deckAfter;
    private static boolean playing = true;

    Player(int playerId) {
        this.playerId = playerId;
        String basePath = (new File("")).getAbsolutePath();
        boolean dir = (new File("outputTextFiles")).mkdir();
        outputFile = new File(String.format("%s/outputTextFiles/player%d_output.txt", basePath, playerId));
        appendToOutputFile(String.format("Player %d enters the game", playerId), false);
    }

    public void appendInitialHand(){
        appendToOutputFile(String.format("player %d initial hand %d %d %d %d",playerId,
                cards.get(0).getCardNumber(),
                cards.get(1).getCardNumber(),
                cards.get(2).getCardNumber(),
                cards.get(3).getCardNumber()), true);
    }

    private void appendToOutputFile(String text, boolean append) {
        try (FileWriter f = new FileWriter(outputFile, append);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) {
            p.println(text); }
        catch (IOException i) { i.printStackTrace(); }
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public synchronized Card drawCard(Card card, int deckId){
        cards.add(card);
        appendToOutputFile(String.format("player %d draws a %d from deck %d",playerId, card.getCardNumber(), deckId), true);
        return card;
    }

    public synchronized Card discardCard(Card card, int deckId) {
        cards.remove(card);
        appendToOutputFile(String.format("player %d discards a %d to deck %d",playerId, card.getCardNumber(), deckId), true);
        return card;
    }

    public synchronized boolean checkHand() {
        for (int i = 0; i < cards.size()-1; i++) {
            if (cards.get(i).getCardNumber() != cards.get(i+1).getCardNumber()){
                return false;
            }
        }
        appendToOutputFile(String.format("player %d wins",this.playerId), true);
        return true;
    }

    public void setDeckBefore(CardDeck deckBefore) {
        this.deckBefore = deckBefore;
    }
    public void setDeckAfter(CardDeck deckAfter) {
        this.deckAfter = deckAfter;
    }

    public synchronized void stopPlayers(){
        playing = false;
        notifyAll();
    }


    public synchronized void run(){
        while(playing){
            deckBefore.removeCard(drawCard(deckBefore.getCardList().get(0),deckBefore.getDeckId()));
            deckAfter.addCard(discardCard(cards.get(0), deckAfter.getDeckId()));
            if (checkHand()){
                stopPlayers();
            }
            // add correct output to string to player
        }


    }
}
