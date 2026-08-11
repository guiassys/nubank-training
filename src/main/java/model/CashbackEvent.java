package model;

public class CashbackEvent implements Comparable<CashbackEvent> {
    private final String transactionId;
    private final String accountId;
    private final int amount;
    private final long maturityTimestamp;
    private boolean cancelled;

    public CashbackEvent(String transactionId, String accountId, int amount, long maturityTimestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.maturityTimestamp = maturityTimestamp;
        this.cancelled = false;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getAmount() {
        return amount;
    }

    public long getMaturityTimestamp() {
        return maturityTimestamp;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public int compareTo(CashbackEvent other) {
        return Long.compare(this.maturityTimestamp, other.maturityTimestamp);
    }
}