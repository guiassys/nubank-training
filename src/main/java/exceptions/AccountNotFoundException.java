package exceptions;

public class AccountNotFoundException extends WalletException {
    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
    }
}
