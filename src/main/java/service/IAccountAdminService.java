package service;

import model.Account;

/**
 * Interface para operações administrativas de contas.
 */
public interface IAccountAdminService {
    boolean create(String accountId);
    Account getAccountById(String accountId);
    boolean unblock(String accountId);
    boolean setDailyLimit(String accountId, int limit);
}