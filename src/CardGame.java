import java.io.*;
import java.util.*;
public class CardGame{

    public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<CardDeck> decks = new ArrayList<>();
    public ArrayList<Card> cards = new ArrayList<>();

    public CardGame() throws IOException {
        setUpGame();
    }

    public CardGame(String test){
        // constructor for test classes to test individual methods rather than running code
    }

    public static void main(String[] args) throws IOException {
        CardGame game = new CardGame();
    }
    public void setUpGame() throws IOException {
        // get num of players and pack file name from user
        Scanner userInput = new Scanner(System.in);  // Create a Scanner object
        int numPlayers = 0;
        System.out.println("Please enter the number of players:");
        boolean invalidInput = true;

        while(invalidInput) { //keep trying until valid input has been entered
            try {
                numPlayers = userInput.nextInt();// Read user input
                invalidInput = false;
            } catch (InputMismatchException e) { // exception happens when input is not int
                System.out.println("Invalid input for players");
                userInput.nextLine();      //eat "invalid input for players" as input so try loop does not think this is the input for numPlayers
            }
        }

        invalidInput = true;
        userInput.nextLine();   //eat whitespace characters so no weird formatting when in between asking for number of players and pack
        while(invalidInput) {
            System.out.println("Please enter location of pack to load:");
            String fileName = userInput.nextLine(); // Read user input
            if (checkPack(numPlayers, fileName)) { // check if pack is valid before reading
                invalidInput = false;
                readPack(fileName);
            }
        }
        initialisePlayersAndDecks(numPlayers);
        dealCards();
        setUpTopology(numPlayers);
        startPlayers(); //start threads
    }

    public void startPlayers() {
        for (Player player: players){player.start();}
    }

    public boolean checkPack(int numPlayers, String fileName) throws IOException {

        boolean valid = true;
        int lineNum = 0;
        int cardNum = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) { //while each line contains something
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

            if (lineNum != (8 * numPlayers)){
                // checks that there are enough cards in the pack
                valid = false;
                System.out.println("Pack does not contain the right amount of cards");
            }

        } catch (FileNotFoundException e) {
            valid = false;
            System.out.println("File not found");
        }
        return valid;
    }

    public void readPack(String fileName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = br.readLine()) != null) {
            int cardNum = Integer.parseInt(line); // get card num from file
            Card card = new Card(cardNum); // creates card object
            cards.add(card); // adds to hashmap cards in CardGame
        }
    }

    public void initialisePlayersAndDecks(int numPlayers){
        // creates directory for players and decks output files
        boolean temp = new File("outputTextFiles").mkdir();
        for (int i = 0; i < numPlayers; i++){
            int id = players.size() + 1; // id cannot be 0 as no card has that value

            Player player = new Player(id); // player/deck creation
            players.add(player);

            CardDeck deck = new CardDeck(id);
            decks.add(deck);
        }
        Player.setNumberOfPlayers(numPlayers); //setup static variable in player class
    }

    public void dealCards(){
        // deals 4 cards to each player
        Collections.shuffle(cards);
        for (int i = 0; i < 4; i++){  //i set as 4 in order to deal 4 cards, iterate through loop 4 times dealing one card each time
            for (Player player : players){
                player.addCard(cards.get(0));
                cards.remove(0);
            }
        }

        // adds initial hand to output file of each player
        for (Player player: players){
            player.appendInitialHand();
        }

        // deals the rest to each deck of cards
        for (int i = 0; i < 4; i++){  //i set as 4 in order to deal 4 cards
            for (CardDeck deck: decks){
                deck.addCard(cards.get(0));
                cards.remove(0);
            }
        }
    }

    public void setUpTopology(int numPlayers){
        players.get(0).setDeckBefore(decks.get(numPlayers-1)); //assign deck before for first player as the last deck
        players.get(0).setDeckAfter(decks.get(0));

        for (int i = 1; i<numPlayers; i++){
            players.get(i).setDeckBefore(decks.get(i-1)); //assign decks to the rest of players
            players.get(i).setDeckAfter(decks.get(i));
        }
    }


}
