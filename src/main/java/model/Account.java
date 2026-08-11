package model;

import java.util.NavigableMap;
import java.util.TreeMap;

public class Account {

    private final String id;
    private int balance;
    private int totalSpent;

    // Mapeia timestamp -> gasto realizado naquele exato instante.
    // Usamos NavigableMap (TreeMap) para consultas eficientes em janelas de tempo O(log N).
    private final NavigableMap<Long, Integer> spendingHistory = new TreeMap<>();

    private static final String AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG = "Amount must be greater than zero.";

    public Account(String id) {
        this.id = id;
        this.balance = 0;
        this.totalSpent = 0;
    }

    public String getId() {
        return this.id;
    }

    public int getBalance() {
        return this.balance;
    }

    public int getTotalSpent() {
        return this.totalSpent;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG);
        }
        this.balance += amount;
    }

    /**
     * Realiza um débito simples (ex: transferência sem timestamp registrado no histórico de janela).
     */
    public boolean withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(AMOUNT_MUST_BE_GREATER_THAN_ZERO_MSG);
        }

        if (balance < amount) {
            return false;
        }

        balance -= amount;
        totalSpent += amount;

        return true;
    }

    /**
     * Realiza um débito associado a um timestamp e o registra no histórico temporal para o SPENT_IN_WINDOW.
     */
    public boolean withdraw(int amount, long timestamp) {
        if (!withdraw(amount)) {
            return false;
        }

        // Acumula os gastos caso existam múltiplos pagamentos no mesmo exato timestamp
        spendingHistory.merge(timestamp, amount, Integer::sum);
        return true;
    }

    /**
     * Processa o estorno/reembolso de uma transação.
     * Devolve o valor ao saldo e reduz o gasto total.
     */
    public void refund(int amount, long timestamp) {
        this.balance += amount;
        this.totalSpent = Math.max(0, this.totalSpent - amount);

        // Se houver registro desse timestamp no histórico, ajustamos também
        if (spendingHistory.containsKey(timestamp)) {
            int currentAtTimestamp = spendingHistory.get(timestamp);
            if (currentAtTimestamp <= amount) {
                spendingHistory.remove(timestamp);
            } else {
                spendingHistory.put(timestamp, currentAtTimestamp - amount);
            }
        }
    }

    /**
     * Calcula o total gasto no intervalo fechado [startTimestamp, endTimestamp].
     */
    public int getSpentInWindow(long startTimestamp, long endTimestamp) {
        if (startTimestamp > endTimestamp) {
            return 0;
        }

        // Obtém apenas a sub-fatia do mapa no intervalo [startTimestamp, endTimestamp]
        NavigableMap<Long, Integer> subMap = spendingHistory.subMap(startTimestamp, true, endTimestamp, true);

        int total = 0;
        for (int amount : subMap.values()) {
            total += amount;
        }
        return total;
    }
}