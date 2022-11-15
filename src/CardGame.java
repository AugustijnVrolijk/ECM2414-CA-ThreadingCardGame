import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


public class CardGame {

    private HashMap<Integer, Player> players = new HashMap<>();
    private HashMap<Integer, Card> cards = new HashMap<>();

    public CardGame() throws IOException {
        startGame();
    }

    public static void main(String[] args) throws IOException {
        CardGame game = new CardGame();
    }


    public void startGame() throws IOException {
        Scanner userInput = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Please enter location of pack to load:");
        String fileName = userInput.nextLine(); // Read user input
        if(checkPack(fileName)){
            readPack(fileName);
        } else {
            System.out.println("Pack Invalid");
        }

        System.out.println("Please enter the number of players:");
        try {
            int players = userInput.nextInt();  // Read user input
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for players");
        }

    }

    public boolean checkPack(String fileName) {
        boolean valid = true;


        return valid;
    }

    public void readPack(String fileName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();


        while ((line = br.readLine()) != null) {
            int cardNum = Integer.parseInt(line);
            int id = cards.size();
            Card card = new Card(cardNum,id);
            cards.put(id, card);
        }
    }

    public void setUpGame(int NumPlayers) {

    }



}
