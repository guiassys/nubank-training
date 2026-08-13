package service;

/**
 * Interface para operações de transações complexas e temporais.
 */
public interface ITransactionService {
    boolean payment(String accountId, int amount, long timestamp);
    String paymentWithCashback(String accountId, int amount, long timestamp, int cashbackPercent);
    boolean refund(String accountId, String transactionId, long timestamp);
    int balance(String accountId, long timestamp);
}