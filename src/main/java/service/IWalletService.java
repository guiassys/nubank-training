package service;

import model.Account;

import java.util.List;

public interface IWalletService {

    boolean create(String accountId);

    Account getAccountById(String accountId);

    int deposit(String accountId, int amount);

    boolean transfer(String from, String to, int amount);

    int balance(String accountId);

    int balance(String accountId, long timestamp);

    boolean payment(String accountId, int amount, long timestamp);

    String paymentWithCashback(String accountId, int amount, long timestamp, int cashbackPercent);

    boolean refund(String accountId, String transactionId, long timestamp);

    int spentInWindow(String accountId, long windowSizeMs, long currentTimestamp);

    List<String> topSpenders(int k);

    boolean unblock(String accountId);

    boolean setDailyLimit(String accountId, int limit);
}