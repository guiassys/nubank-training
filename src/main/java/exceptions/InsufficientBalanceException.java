package exceptions;

public class InsufficientBalanceException extends WalletException {
    public InsufficientBalanceException(String accountId, int required, int available) {
        super(String.format("Insufficient balance for account %s. Required: %d, Available: %d", accountId, required, available));
    }
}