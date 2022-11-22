public class Card {

    private int cardId;
    private int cardNumber;

    public Card(int cardNumber, int cardId) {
        synchronized (Card.class){
            this.cardId = cardId;
            this.cardNumber = cardNumber;
        }
    }
    public int getCardNumber() {
        return cardNumber;
    }
    @Override
    public String toString() {
        return String.valueOf(cardNumber);
    }
}
