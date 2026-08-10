import java.util.HashMap;
import java.util.Map;

public class WalletService {

    private final Map<String, Account> accounts = new HashMap<>();
    private static final String SEPARATOR = "--------------------------------------";

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
     * @return Account object or null if account not found
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

    public void printAccounts(){
        System.out.println(SEPARATOR);
        accounts.forEach((accountId, account) -> {
            System.out.println("Saldo da conta " + accountId + ": " + account.getBalance());
        });
        System.out.println(SEPARATOR);
    }

}