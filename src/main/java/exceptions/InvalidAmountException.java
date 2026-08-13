package exceptions;

public class InvalidAmountException extends WalletException {
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(int amount) {
        super("Invalid amount: " + amount + ". Amount must be greater than zero.");
    }
}