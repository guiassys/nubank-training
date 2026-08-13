package exceptions;

public class DailyLimitExceededException extends WalletException {
    public DailyLimitExceededException(String accountId, int amount, int limit, int spentToday) {
        super(String.format("Daily limit exceeded for account %s. Limit: %d, Already spent today: %d, Requested: %d",
                accountId, limit, spentToday, amount));
    }
}