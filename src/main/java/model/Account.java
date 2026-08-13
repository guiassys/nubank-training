package model;

import java.util.ArrayDeque;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.TreeMap;

public class Account {

    private final String id;
    private int balance;
    private int totalSpent;
    private boolean locked;
    private int dailyLimit;

    private final Queue<Long> debitTimestamps;
    private final NavigableMap<Long, Integer> spendingHistory = new TreeMap<>();
    private final NavigableMap<Long, Integer> dailySpending = new TreeMap<>();

    private static final String AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG = "Amount must be greater than zero.";
    private static final int FRAUD_TRANSACTION_LIMIT = 3;
    private static final long FRAUD_TIME_WINDOW_MS = 120_000L;
    private static final long DAY_IN_MS = 86_400_000L;

    public Account(String id) {
        this.id = id;
        this.balance = 0;
        this.totalSpent = 0;
        this.locked = false;
        this.dailyLimit = -1;
        this.debitTimestamps = new ArrayDeque<>();
    }

    public String getId() {
        return this.id;
    }

    public synchronized int getBalance() {
        return this.balance;
    }

    public synchronized int getTotalSpent() {
        return this.totalSpent;
    }

    public synchronized boolean isLocked() {
        return locked;
    }

    public synchronized void lock() {
        this.locked = true;
        this.debitTimestamps.clear();
    }

    public synchronized void unlock() {
        this.locked = false;
        this.debitTimestamps.clear();
    }

    public synchronized void setDailyLimit(int limit) {
        this.dailyLimit = limit;
    }

    public synchronized void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG);
        }
        this.balance += amount;
    }

    public synchronized boolean withdraw(int amount) {
        return doWithdraw(amount, System.currentTimeMillis(), false);
    }

    public synchronized boolean withdraw(int amount, long timestamp) {
        return doWithdraw(amount, timestamp, true);
    }

    private boolean doWithdraw(int amount, long timestamp, boolean recordForSpentInWindow) {
        if (locked) {
            return false;
        }

        cleanOldDebitTimestamps(timestamp);

        if (debitTimestamps.size() >= FRAUD_TRANSACTION_LIMIT) {
            lock();
            return false;
        }

        if (isDailyLimitExceeded(amount, timestamp)) {
            return false;
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG);
        }
        if (balance < amount) {
            return false;
        }
        balance -= amount;
        totalSpent += amount;

        debitTimestamps.offer(timestamp);
        dailySpending.merge(timestamp / DAY_IN_MS, amount, Integer::sum);
        if (recordForSpentInWindow) {
            spendingHistory.merge(timestamp, amount, Integer::sum);
        }

        return true;
    }

    public synchronized void refund(int amount, long timestamp) {
        this.balance += amount;
        this.totalSpent = Math.max(0, this.totalSpent - amount);

        if (spendingHistory.containsKey(timestamp)) {
            int recordedAmountAtTimestamp = spendingHistory.get(timestamp);
            if (recordedAmountAtTimestamp <= amount) {
                spendingHistory.remove(timestamp);
            } else {
                spendingHistory.put(timestamp, recordedAmountAtTimestamp - amount);
            }
        }

        long dayKey = timestamp / DAY_IN_MS;
        if (dailySpending.containsKey(dayKey)) {
            int currentDailySpent = dailySpending.get(dayKey);
            dailySpending.put(dayKey, Math.max(0, currentDailySpent - amount));
        }
    }

    public synchronized int getSpentInWindow(long startTimestamp, long endTimestamp) {
        if (startTimestamp > endTimestamp) {
            return 0;
        }
        NavigableMap<Long, Integer> subMap = spendingHistory.subMap(startTimestamp, true, endTimestamp, true);
        int total = 0;
        for (int value : subMap.values()) {
            total += value;
        }
        return total;
    }

    private void cleanOldDebitTimestamps(long currentTimestamp) {
        while (!debitTimestamps.isEmpty() && debitTimestamps.peek() < currentTimestamp - FRAUD_TIME_WINDOW_MS) {
            debitTimestamps.poll();
        }
    }

    private boolean isDailyLimitExceeded(int amount, long timestamp) {
        if (dailyLimit < 0) {
            return false;
        }
        long dayKey = timestamp / DAY_IN_MS;
        int spentToday = dailySpending.getOrDefault(dayKey, 0);
        return (spentToday + amount) > dailyLimit;
    }
}