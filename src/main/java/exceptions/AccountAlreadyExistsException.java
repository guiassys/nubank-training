package exceptions;

public class AccountAlreadyExistsException extends WalletException {
    public AccountAlreadyExistsException(String accountId) {
        super("Account already exists: " + accountId);
    }
}
