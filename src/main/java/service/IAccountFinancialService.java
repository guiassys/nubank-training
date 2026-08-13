package service;

/**
 * Interface para operações financeiras básicas de contas.
 */
public interface IAccountFinancialService {
    int deposit(String accountId, int amount);
    boolean transfer(String from, String to, int amount);
    int balance(String accountId);
}