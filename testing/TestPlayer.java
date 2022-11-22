import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class TestPlayer {

    private Player player1;
    private Player player2;
    private CardDeck deck1;
    private Card card0;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;
    private Card card5;
    private Card card6;
    private Card card7;
    String basePath = (new File("")).getAbsolutePath();

    @Before
    public void setUp() {
        player1 = new Player(1);
        player2 = new Player(2);
        deck1 = new CardDeck(0);
        card0 = new Card(1);
        card1 = new Card(1);
        card2 = new Card(1);
        card3 = new Card(1);
        card4 = new Card(2);
        card5 = new Card(3);
        card6 = new Card(4);
        card7 = new Card(5);
        Player.setNumberOfPlayers(2);
        player2.addCard(card4);
        player2.addCard(card5);
        player2.addCard(card6);
        player2.addCard(card7);
    }


    public String CheckOutputFile(String fileName, int LineNumber) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        for(int i = 0; i < LineNumber; i++) {
            line = br.readLine();
        }
        line = br.readLine();
        return line;
    }

    @Test
    public void testInitialPlayerSetup() throws IOException {
        assert(Objects.equals(CheckOutputFile(String.format("%s/outputTextFiles/player1_output.txt", basePath),0), "Player 1 enters the game"));
        assert(Objects.equals(CheckOutputFile(String.format("%s/outputTextFiles/player2_output.txt", basePath),0), "Player 2 enters the game"));
    }
    @Test
    public void testDrawCard() {
        assert (player2.getCards().size()==4);
    }
    @Test
    public void testAppendInitialHand() throws IOException {
        player1.appendInitialHand();
        player2.appendInitialHand();
        assert(Objects.equals(CheckOutputFile(String.format("%s/outputTextFiles/player1_output.txt", basePath),1), "player 1 initial hand []"));
        assert(Objects.equals(CheckOutputFile(String.format("%s/outputTextFiles/player2_output.txt", basePath),1), "player 2 initial hand [2, 3, 4, 5]"));
    }
    @Test
    public void testDiscardCard() {
        player2.discardCard();
        assert (player1.getCards().size()==0);
    }
    @Test
    public void testAppendToOutputFile() {
    }
    @Test
    public void testCheckHand() {
        player1.addCard(card0);
        player1.addCard(card1);
        player1.addCard(card2);
        player1.addCard(card3);
        assert player1.checkHand();
    }
}
