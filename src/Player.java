import java.util.ArrayList;
import java.io.*;
import java.util.Random;

public class Player extends Thread {
    private int playerId;
    private ArrayList<Card> cards = new ArrayList<>();
    private File outputFile;
    private CardDeck deckBefore;
    //take cards from
    private CardDeck deckAfter;
    //remove cards to
    private int preferredCard;
    private static boolean playing = true;
    Random rand = new Random();
    private boolean hasDrawnCard = false;
    private boolean hasDiscardedCard = false;
    private static int isReady = 0;
    static Object lock = new Object();

    Player(int playerId) {
        this.playerId = playerId;
        this.preferredCard = playerId;
        String basePath = (new File("")).getAbsolutePath();
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

    public synchronized Card drawCard() {
        for(int i = 0; i < deckBefore.getCardList().size(); i++) {
            if (deckBefore.getCardList().get(i).getCardNumber() == preferredCard) {
                System.out.println("player "+ playerId + " has drawn a " + deckBefore.getCardList().get(i).getCardNumber());
                cards.add(deckBefore.getCardList().get(i));
                appendToOutputFile(String.format("player %d draws a %d from deck %d",playerId, deckBefore.getCardList().get(i).getCardNumber(), deckBefore.getDeckId()), true);
                return deckBefore.getCardList().get(i);
            }
        }
        int i = rand.nextInt(deckBefore.getCardList().size());
        System.out.println("player "+ playerId + " has drawn a " + deckBefore.getCardList().get(i).getCardNumber());
        cards.add(deckBefore.getCardList().get(i));
        appendToOutputFile(String.format("player %d draws a %d from deck %d",playerId, deckBefore.getCardList().get(i).getCardNumber(), deckBefore.getDeckId()), true);
        return deckBefore.getCardList().get(i);
    }

    public synchronized Card discardCard() {
        ArrayList<Card> temp = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCardNumber() != preferredCard) {
                temp.add(cards.get(i));
            }
        }
        int i = rand.nextInt(temp.size());
        System.out.println("player "+ playerId + " has discarded a " + temp.get(i).getCardNumber());
        cards.remove(temp.get(i));
        appendToOutputFile(String.format("player %d discards a %d to deck %d",playerId, temp.get(i).getCardNumber(), deckAfter.getDeckId()), true);
        return temp.get(i);
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

    public void setDeckBefore(CardDeck deckBefore) {
        this.deckBefore = deckBefore;
    }
    public void setDeckAfter(CardDeck deckAfter) {
        this.deckAfter = deckAfter;
    }

    public void stopPlayers(){
        playing = false;
        notifyAll();
    }
    public synchronized void run() {
        while(playing){
            if (checkHand()){
                stopPlayers();
                deckAfter.recordFinalHand();
            }

            if(!hasDrawnCard) {
                deckBefore.removeCard(drawCard());
                hasDrawnCard = true;
                System.out.println("Deck " + deckBefore.getDeckId() + " has is now of size " + deckBefore.getCardList().size());
            }

            if(!hasDiscardedCard) {
                deckAfter.addCard(discardCard());
                hasDiscardedCard = true;
                System.out.println("Deck " + deckAfter.getDeckId() + " has is now of size " + deckAfter.getCardList().size());
            }

            if(hasDrawnCard && hasDiscardedCard) {
                hasDiscardedCard = false;
                hasDrawnCard = false;
                isReady += 1;

                synchronized (lock) {
                    if (isReady < 4) { //number of players
                        try {
                            System.out.println("player " + playerId + " has gotten to the wait clause, number of threads which have said isReady is : " + isReady);
                            lock.wait();
                            System.out.println("");}
                        catch (InterruptedException ignored){}
                    } else {
                        System.out.println("player " + playerId + " has gotten to the else clause, isReady is currently equal to " + isReady);
                        lock.notifyAll();
                        isReady = 0;
                    }
                }

            }
        }
    }
}
