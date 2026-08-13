package exceptions;

public class AccountLockedException extends WalletException {
    public AccountLockedException(String accountId) {
        super("Account is locked: " + accountId);
    }
}