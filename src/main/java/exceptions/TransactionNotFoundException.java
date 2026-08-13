package exceptions;

public class TransactionNotFoundException extends WalletException {
    public TransactionNotFoundException(String transactionId) {
        super("Transaction not found: " + transactionId);
    }
}