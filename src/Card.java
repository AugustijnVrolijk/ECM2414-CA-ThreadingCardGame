public class Card {
    private int cardNumber;

    public Card(int cardNumber) {
        synchronized (Card.class){
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
