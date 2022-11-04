public class Card {

    private static int id;
    private int cardId;
    private int cardNumber;

    public Card(int cardNumber) {
        this.cardId = id++;
        this.cardNumber = cardNumber;
    }
}
