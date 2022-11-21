import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CardGame{

    // have added a hash map to access decks and players easily by id
    private HashMap<Integer, Player> players = new HashMap<>();
    private HashMap<Integer, CardDeck> decks = new HashMap<>();
    private ArrayList<Card> cards = new ArrayList<>();


    public CardGame() throws IOException {
        setUpGame();
    }

    // constructor for test classes to test individual functions rather than running code
    public CardGame(String test){}

    public static void main(String[] args) throws IOException {
        CardGame game = new CardGame();
    }

    public void setUpGame() throws IOException {

        // get num of players and pack file name from user
        Scanner userInput = new Scanner(System.in);  // Create a Scanner object
        int numPlayers = 0;

        System.out.println("Please enter the number of players:");
        try {
            numPlayers = userInput.nextInt();  // Read user input
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for players");
        }

        System.out.println("Please enter location of pack to load:");
        userInput.nextLine();
        String fileName = userInput.nextLine(); // Read user input
        if(checkPack(numPlayers, fileName)){ // check if pack is valid before reading
            readPack(fileName);
        } else {
            System.out.println("Pack Invalid");
        }

        initialisePlayersAndDecks(numPlayers);
        dealCards();
        setUpTopology(numPlayers);
        startPlayers(); //start threads

    }

    public void startPlayers() {
        for (Player player: players.values()){player.start();}
    }


    public boolean checkPack(int numPlayers, String fileName) throws IOException {

        boolean valid = true;
        int lineNum = 0;
        int cardNum = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                lineNum++; // count lines
                try {
                    cardNum = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Pack contains non-int");
                    // error will happen if the line does not contain a number
                    valid = false;
                }
                if (cardNum == 0) {
                    System.out.println("Pack contains 0");
                    valid = false;
                }
            }

            if (lineNum < (8 * numPlayers)){
                // checks that there are enough cards in the pack
                valid = false;
                System.out.println("Pack does not contain enough cards");
            }

        } catch (FileNotFoundException e) {
            valid = false;
            System.out.println("File not found");
        }
        return valid;
    }

    public void readPack(String fileName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();

        while ((line = br.readLine()) != null) {
            int cardNum = Integer.parseInt(line); // get card num from file
            int id = cards.size();
            Card card = new Card(cardNum,id); // creates card object
            cards.add(card); // adds to hashmap cards in CardGame
        }
    }

    public void initialisePlayersAndDecks(int numPlayers){
        for (int i = 0; i < numPlayers; i++){
            int id = players.size() + 1; // don't want an id of 0 as there are no cards with value 0

            Player player = new Player(id); // thread created
            players.put(id,player);

            CardDeck deck = new CardDeck(id);
            decks.put(id, deck);
        }
    }

    public void dealCards(){
        // deals 4 cards to each player
        Collections.shuffle(cards);
        for (int i = 0; i < 4; i++){
            for (Player player : players.values()){
                player.addCard(cards.get(0));
                cards.remove(0);
            }
        }

        // adds initial hand to output file of each player
        for (Player player: players.values()){
            player.appendInitialHand();
        }

        // deals the rest to each deck of cards
        for (CardDeck deck: decks.values()){
            try {
                deck.addCard(cards.get(0));
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    public void setUpTopology(int numPlayers){
        players.get(0).setDeckBefore(decks.get(numPlayers-1));
        players.get(0).setDeckAfter(decks.get(0));

        for (int i = 1; i<numPlayers; i++){
            players.get(i).setDeckBefore(decks.get(i-1));
            players.get(i).setDeckAfter(decks.get(i));
        }
    }

}
