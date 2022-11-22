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
    Random rand = new Random();
    private boolean hasPlayed = false;
    private static boolean playing = true;
    private static int isReady = 0;
    private static int winningThread;
    private static int numberOfPlayers;
    static Object lock = new Object();

    Player(int playerId) {
        this.playerId = playerId;
        this.preferredCard = playerId;
        String basePath = (new File("")).getAbsolutePath();
        outputFile = new File(String.format("%s/outputTextFiles/player%d_output.txt", basePath, playerId));
        appendToOutputFile(String.format("Player %d enters the game", playerId), false);
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Player.numberOfPlayers = numberOfPlayers;
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
        winningThread = playerId;
        lock.notifyAll();
    }

    private synchronized void incrementIsReady() {
        isReady += 1;
    }

    private synchronized void resetIsReady() {
        isReady = 0;
    }
    @Override
    public void run() {
        while(playing){
            synchronized (lock) {
                if (checkHand()){
                    System.out.println("The game has apparently been won by player " + playerId);
                    stopPlayers();
                    appendToOutputFile(String.format("player %d exits",this.playerId), true);
                    appendToOutputFile(String.format("Player %d final hand: %d %d %d %d",
                            playerId,
                            cards.get(0).getCardNumber(),
                            cards.get(1).getCardNumber(),
                            cards.get(2).getCardNumber(),
                            cards.get(3).getCardNumber()), true);
                    deckAfter.recordFinalHand();
                    stop();
                }

                if(!hasPlayed) {
                    deckBefore.removeCard(drawCard());
                    deckAfter.addCard(discardCard());
                    hasPlayed = true;
                    System.out.println("Deck " + deckBefore.getDeckId() + " has is now of size " + deckBefore.getCardList().size());
                    System.out.println("Deck " + deckAfter.getDeckId() + " has is now of size " + deckAfter.getCardList().size());
                }
                if(hasPlayed) {
                    incrementIsReady();
                    hasPlayed = false;
                    if (isReady < numberOfPlayers) { //number of players
                        try {
                            System.out.println("player " + playerId + " has gotten to the wait clause, number of threads which have said isReady is : " + isReady);
                            lock.wait();
                            System.out.println("");}
                        catch (InterruptedException ignored){System.out.println("Interrupted Exception caught");}
                    } else {
                        System.out.println("player " + playerId + " has gotten to the else clause, isReady is currently equal to " + isReady);
                        lock.notifyAll();
                        resetIsReady();
                    }
                }

            }
        }
        appendToOutputFile(String.format("player %d has informed player %d that player %d has won",winningThread, this.playerId, winningThread), true);
        appendToOutputFile(String.format("player %d exits",this.playerId), true);
        appendToOutputFile(String.format("Player %d final hand: %d %d %d %d",
                playerId,
                cards.get(0).getCardNumber(),
                cards.get(1).getCardNumber(),
                cards.get(2).getCardNumber(),
                cards.get(3).getCardNumber()), true);
        deckAfter.recordFinalHand();
        stop();
    }
}
