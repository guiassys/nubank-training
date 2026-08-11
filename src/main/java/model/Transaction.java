package model;

public class Transaction {
    private final String id;
    private final String accountId;
    private final int amount;
    private final long timestamp;
    private final int cashbackAmount;
    private boolean refunded;

    public Transaction(String id, String accountId, int amount, long timestamp, int cashbackAmount) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.cashbackAmount = cashbackAmount;
        this.refunded = false;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }
}