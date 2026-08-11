package service;

import model.Account;

import java.util.*;

public class WalletService {

    private final Map<String, Account> accounts = new HashMap<>();

    /**
     * Creates a new account.
     *
     * @return true if the account was created,
     * false if an account with the same id already exists.
     */
    public boolean create(String accountId){
        if(accounts.containsKey(accountId)){
           return false;
        }
        Account account = new Account(accountId);
        accounts.put(accountId, account);
        return true;
    }

    /**
     * Get account by id.
     *
     * @return model.Account object or null if account not found
     */
    public Account getAccountById(String accountId){
        return accounts.get(accountId);
    }

    /**
     * Deposit a new amount in account balance.

     * @return -1 if the account is not found or the new balance otherwise if account existis.
     */
    public int deposit(String accountId, int amount){
        Account account = getAccountById(accountId);
        if(account == null){
            return -1;
        }
        account.deposit(amount);
        return account.getBalance();
    }

    /**
     * Transfer values between two accounts
     *
     * @return true if the transfer was success, false if transfer was not success
     */
    public boolean transfer(String from, String to, int amount){
        Account fromAccount = getAccountById(from);
        Account toAccount = getAccountById(to);

        if(fromAccount == null || toAccount == null){
            return false;
        }

        if(from.equals(to)){
            return false;
        }

        if(amount <= 0){
            return false;
        }

        if (!fromAccount.withdraw(amount)) {
            return false;
        }

        toAccount.deposit(amount);

        return true;
    }

    /**
     * Get balance applying filter by accountId
     *
     * @return -1 if the account is not found or the balance value otherwise
     */
    public int balance(String accountId){
        Account account = getAccountById(accountId);
        if(account == null){
            return -1;
        }
        return account.getBalance();
    }

    public boolean payment(String accountId, int amount, long timestamp){
        if(amount <= 0){
            return false;
        }
        Account account = getAccountById(accountId);
        if(account == null){
            return false;
        }
        return account.withdraw(amount);
    }

    /**
     * Comparator para a Min-Heap do Top K.
     * O "pior" candidato do Top K fica na raiz (topo):
     * 1. Menor gasto total (totalSpent).
     * 2. Em caso de empate, o ID lexicograficamente MAIOR (ex: "B" e "A" -> "B" fica no topo).
     */
    private static final Comparator<Account> MIN_HEAP_SPENDER_COMPARATOR = (a1, a2) -> {
        if (a1.getTotalSpent() != a2.getTotalSpent()) {
            return Integer.compare(a1.getTotalSpent(), a2.getTotalSpent());
        }
        return a2.getId().compareTo(a1.getId());
    };

    public List<String> topSpenders(int k) {
        if (k <= 0) {
            return Collections.emptyList();
        }

        PriorityQueue<Account> minHeap = new PriorityQueue<>(MIN_HEAP_SPENDER_COMPARATOR);

        for (Account account : accounts.values()) {
            if (account.getTotalSpent() <= 0) {
                continue;
            }

            minHeap.offer(account);

            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        List<Account> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll());
        }

        Collections.reverse(result);

        return result.stream()
                .map(acc -> acc.getId() + "(" + acc.getTotalSpent() + ")")
                .toList();
    }



}