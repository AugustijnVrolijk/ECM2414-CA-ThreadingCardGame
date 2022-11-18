import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CardGame {

    private HashMap<Integer, Player> players = new HashMap<>();
    private HashMap<Integer, Card> cards = new HashMap<>();

    public CardGame() throws IOException {
        setUpGame();
    }

    public static void main(String[] args) throws IOException {
        CardGame game = new CardGame();
    }


    public void setUpGame() throws IOException {
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

        createPlayers(numPlayers);
    }

    public boolean checkPack(int numPlayers, String fileName) throws IOException {

        boolean valid = true;
        int lineNum = 0;
        int cardNum = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                lineNum ++; // count lines
                try {
                    cardNum = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    // error will happen if the line does not contain a number
                    valid = false;
                }
                if (cardNum == 0) {
                    valid = false;
                }
            }

            if (lineNum < (8 * numPlayers)){
                // checks that there are enough cards in the pack
                valid = false;
            }

        } catch (FileNotFoundException e) {
            valid = false;
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
            cards.put(id, card); // adds to hashmap cards in CardGame
        }
    }

    public void createPlayers(int numPlayers){
        for (int i = 0; i < numPlayers; i++){
            int id = players.size();
            Player player = new Player(id);
            players.put(id,player);
        }
    }

    public void dealCards() {
        for (int i = 0; i < 4; i++){
            for (Player player : players.values()){

            }
        }
    }
}
