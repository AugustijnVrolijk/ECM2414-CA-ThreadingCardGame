public class Card {

    private int cardId;
    private int cardNumber;

    public Card(int cardNumber, int cardId) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }
    public int getCardId() {
        return cardId;
    }
}
