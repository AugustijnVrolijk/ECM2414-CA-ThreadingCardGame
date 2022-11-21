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
    private int preferredCard = playerId;
    private static boolean playing = true;
    Random rand = new Random();
    private boolean hasDrawnCard = true;
    private boolean hasDiscardedCard = true;
    private static int isReady = 0;

    Player(int playerId) {
        this.playerId = playerId;
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

    public Card drawCard() {
        for(int i = 0; i < deckBefore.getCardList().size(); i++) {
            if (deckBefore.getCardList().get(i).getCardNumber() == preferredCard) {
                cards.add(deckBefore.getCardList().get(i));
                appendToOutputFile(String.format("player %d draws a %d from deck %d",playerId, deckBefore.getCardList().get(i).getCardNumber(), deckBefore.getDeckId()), true);
                return deckBefore.getCardList().get(i);
            }
        }
        System.out.println(deckBefore.getCardList().size());
        int i = rand.nextInt(deckBefore.getCardList().size());
        cards.add(deckBefore.getCardList().get(i));
        appendToOutputFile(String.format("player %d draws a %d from deck %d",playerId, deckBefore.getCardList().get(i).getCardNumber(), deckBefore.getDeckId()), true);
        return deckBefore.getCardList().get(i);
    }

    public Card discardCard(Card card) {
        cards.remove(card);
        appendToOutputFile(String.format("player %d discards a %d to deck %d",playerId, card.getCardNumber(), deckAfter.getDeckId()), true);
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

    private int pickDiscardedCard() { //throws Exception
        int index = 0;
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCardNumber() != preferredCard) {
                temp.add(i);
            }
        }
        return temp.get(rand.nextInt(temp.size()));
        //throw new Exception("Exception message");
    }

    public synchronized void run() {
        while(playing){
            if (checkHand()){
                stopPlayers();
                deckAfter.recordFinalHand();
            }

            if(!hasDrawnCard) {
                synchronized (deckBefore) {
                    try {
                        deckBefore.removeCard(drawCard());
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                    hasDrawnCard = true;
                }
            }
            if(!hasDiscardedCard) {
                synchronized (deckAfter) {
                    try {
                        deckAfter.addCard(discardCard(cards.get(pickDiscardedCard())));
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                    hasDiscardedCard = true;
                }
            }
            if(hasDrawnCard && hasDiscardedCard) {
                hasDiscardedCard = false;
                hasDrawnCard = false;
                isReady += 1;

                if (isReady < 4) { //number of players
                    try {
                        System.out.println("wait was successfull I think");
                        System.out.println(playerId);
                        wait();
                        System.out.println("wait did not work wtf");}
                    catch (InterruptedException ignored){}
                } else {
                    notifyAll();
                }
            }
        }
    }
}
