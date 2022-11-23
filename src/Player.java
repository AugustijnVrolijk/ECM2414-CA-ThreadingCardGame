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
        outputFile = new File(String.format("%s/outputTextFiles/player%d_output.txt", basePath, playerId)); //create output file to output folder
        appendToOutputFile(String.format("Player %d enters the game", playerId), false); //finish setup in text file declaring player is created
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Player.numberOfPlayers = numberOfPlayers;
    }

    public void appendInitialHand(){
        appendToOutputFile(String.format("player %d initial hand %s",playerId, cards.toString()), true);
    }

    public int getPlayerId() {
        return playerId;
    }

    public CardDeck getDeckAfter() {
        return deckAfter;
    }

    private void appendToOutputFile(String text, boolean append) {
        try (FileWriter f = new FileWriter(outputFile, append);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) { //using .io library (FileWriter) to write to text file, bufferedWriter and PrintWriter needed to append to file
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
            if (deckBefore.getCardList().get(i).getCardNumber() == preferredCard) { //iterate through cards in deck before, check if any of those cards are the preferred card
                cards.add(deckBefore.getCardList().get(i));
                appendToOutputFile(String.format("player %d draws a %d from deck %d",playerId, deckBefore.getCardList().get(i).getCardNumber(), deckBefore.getDeckId()), true);
                return deckBefore.getCardList().get(i); //return the card object, so it can be removed from the deck before - to avoid duplicates
            }
        }
        int i = rand.nextInt(deckBefore.getCardList().size()); //choose random card as none meet the requirements for this player
        cards.add(deckBefore.getCardList().get(i));
        appendToOutputFile(String.format("player %d draws a %d from deck %d",playerId, deckBefore.getCardList().get(i).getCardNumber(), deckBefore.getDeckId()), true);
        return deckBefore.getCardList().get(i);
    }

    public synchronized Card discardCard() {
        ArrayList<Card> temp = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCardNumber() != preferredCard) { //check which cards which this player holds don't meet the requirements
                temp.add(cards.get(i));
            }
        }
        int i = rand.nextInt(temp.size()); //choose random card to discard from ones which don't meet the requirements to avoid the game stagnating
        cards.remove(temp.get(i));
        appendToOutputFile(String.format("player %d discards a %d to deck %d",playerId, temp.get(i).getCardNumber(), deckAfter.getDeckId()), true);
        return temp.get(i); //return the discarded cards, so it can be added to the deck after
    }

    public boolean checkHand() {
        for (int i = 0; i < cards.size()-1; i++) {
            if (cards.get(i).getCardNumber() != cards.get(i+1).getCardNumber()){ //check if any of the cards in a player's hand are different to each other
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
        playing = false; //turns playing false so main loop in run stops
        winningThread = playerId; //sets this player as the winning player to inform others
        lock.notifyAll(); //removes lock in case other threads are waiting for others to finish
    }

    private synchronized void incrementIsReady() {
        isReady += 1;
    } // synchronised so it isReady count stays accurate

    private synchronized void resetIsReady() {
        isReady = 0;
    }
    @Override
    public void run() {
        while(playing){
            synchronized (lock) {
                if (checkHand()){
                    System.out.printf("Player %d wins%n", playerId);
                    appendToOutputFile(String.format("player %d exits", playerId), true);
                    appendToOutputFile(String.format("Player %d final hand: %s", playerId, cards.toString()), true);
                    deckAfter.recordFinalHand();
                    stopPlayers();
                    stop();
                }

                if(!hasPlayed) {
                    deckBefore.removeCard(drawCard());
                    deckAfter.addCard(discardCard());
                    hasPlayed = true;
                }
                if(hasPlayed) {
                    incrementIsReady();
                    hasPlayed = false;
                    if (isReady < numberOfPlayers) { //check if all other players have finished their turn, wait if they haven't
                        try {lock.wait();}
                        catch (InterruptedException ignored){System.out.println("Interrupted Exception caught");}
                    } else {
                        lock.notifyAll(); //every player has now finished their turn, they can continue to their next turn
                        resetIsReady();
                    }
                }

            }
        }
        appendToOutputFile(String.format("player %d has informed player %d that player %d has won",winningThread, this.playerId, winningThread), true);
        appendToOutputFile(String.format("player %d exits",this.playerId), true);
        appendToOutputFile(String.format("Player %d final hand: %s", playerId, cards.toString()), true);
        deckAfter.recordFinalHand();
        stop();
    }
}
