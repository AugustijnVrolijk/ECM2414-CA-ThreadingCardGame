public class Card {

    private static int id = 0;
    private int cardId;
    private int cardNumber;

    public Card(int cardNumber) {
        this.cardId = id++;
        this.cardNumber = cardNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }
}
