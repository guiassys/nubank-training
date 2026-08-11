package model;

public class Account {

    private final String id;
    private int balance;
    private int totalSpent;
    private static final String AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG = "Amount must be greater than zero.";

    public Account(String id){
        this.id = id;
        this.balance = 0;
        this.totalSpent = 0;
    }

    public String getId(){
        return this.id;
    }

    public int getBalance(){
        return this.balance;

    }

    public int getTotalSpent(){
        return this.totalSpent;
    }

    public void deposit(int amount){
        if(amount <= 0){
            throw new IllegalArgumentException(AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG);
        }
        this.balance += amount;
    }

    public boolean withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG
            );
        }

        if (balance < amount) {
            return false;
        }

        balance -= amount;
        totalSpent += amount;

        return true;
    }

}