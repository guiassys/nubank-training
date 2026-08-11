package model;

public class Account {

    private final String id;
    private int balance;
    private static final String AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG = "Amount must be greater than zero.";

    public Account(String id){
        this.id = id;
        this.balance = 0;
    }

    public String getId(){
        return this.id;
    }

    public int getBalance(){
        return this.balance;

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

        return true;
    }

}