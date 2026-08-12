package service;

import model.Account;
import model.CashbackEvent;
import model.Transaction;

import java.util.*;

public class WalletService implements IWalletService {

    private final Map<String, Account> accounts = new HashMap<>();

    // Mapeamento idTransacao -> Objeto Transaction para permitir o REFUND
    private final Map<String, Transaction> transactions = new HashMap<>();

    // Fila de prioridade de cashbacks ordenados pelo tempo de maturação (maturityTimestamp)
    private final PriorityQueue<CashbackEvent> cashbackQueue = new PriorityQueue<>();

    // Mapeamento idTransacao -> CashbackEvent para localizar e cancelar rapidamente durante o REFUND
    private final Map<String, CashbackEvent> pendingCashbacks = new HashMap<>();

    private int transactionSequence = 0;
    private static final long CASHBACK_DELAY_MS = 86_400_000L; // 24 horas em milissegundos

    /**
     * Processa todos os cashbacks pendentes que maturaram até o timestamp atual.
     */
    private void processPendingCashbacks(long currentTimestamp) {
        while (!cashbackQueue.isEmpty() && cashbackQueue.peek().getMaturityTimestamp() <= currentTimestamp) {
            CashbackEvent event = cashbackQueue.poll();

            // Remove do mapa de pendentes
            pendingCashbacks.remove(event.getTransactionId());

            // Se o cashback foi cancelado (via REFUND), apenas descarta
            if (event.isCancelled()) {
                continue;
            }

            Account account = accounts.get(event.getAccountId());
            if (account != null && event.getAmount() > 0) {
                account.deposit(event.getAmount());
            }
        }
    }

    public boolean create(String accountId) {
        if (accounts.containsKey(accountId)) {
            return false;
        }
        Account account = new Account(accountId);
        accounts.put(accountId, account);
        return true;
    }

    public Account getAccountById(String accountId) {
        return accounts.get(accountId);
    }

    public int deposit(String accountId, int amount) {
        Account account = getAccountById(accountId);
        if (account == null) {
            return -1;
        }
        account.deposit(amount);
        return account.getBalance();
    }

    public boolean transfer(String from, String to, int amount) {
        Account fromAccount = getAccountById(from);
        Account toAccount = getAccountById(to);

        if (fromAccount == null || toAccount == null) {
            return false;
        }

        if (from.equals(to) || amount <= 0) {
            return false;
        }

        if (!fromAccount.withdraw(amount)) {
            return false;
        }

        toAccount.deposit(amount);
        return true;
    }

    public int balance(String accountId) {
        Account account = getAccountById(accountId);
        if (account == null) {
            return -1;
        }
        return account.getBalance();
    }

    /**
     * NÍVEL 3 — Consulta o saldo processando eventos temporais (cashbacks)
     * pendentes que maturaram até o timestamp informado.
     */
    public int balance(String accountId, long timestamp) {
        processPendingCashbacks(timestamp);
        return balance(accountId);
    }

    public boolean payment(String accountId, int amount, long timestamp) {
        processPendingCashbacks(timestamp);

        if (amount <= 0) {
            return false;
        }
        Account account = getAccountById(accountId);
        if (account == null) {
            return false;
        }

        return account.withdraw(amount, timestamp);
    }

    /**
     * NÍVEL 3 — PAYMENT WITH CASHBACK
     * Realiza um pagamento e agenda o crédito do cashback para timestamp + 24 horas.
     */
    public String paymentWithCashback(String accountId, int amount, long timestamp, int cashbackPercent) {
        processPendingCashbacks(timestamp);

        if (amount <= 0 || cashbackPercent < 0 || cashbackPercent > 100) {
            return null;
        }

        Account account = getAccountById(accountId);
        if (account == null) {
            return null;
        }

        if (!account.withdraw(amount, timestamp)) {
            return null;
        }

        int cashbackAmount = (amount * cashbackPercent) / 100;
        String transactionId = "TX-" + (++transactionSequence);

        Transaction transaction = new Transaction(transactionId, accountId, amount, timestamp, cashbackAmount);
        transactions.put(transactionId, transaction);

        if (cashbackAmount > 0) {
            long maturityTimestamp = timestamp + CASHBACK_DELAY_MS;
            CashbackEvent event = new CashbackEvent(transactionId, accountId, cashbackAmount, maturityTimestamp);
            cashbackQueue.offer(event);
            pendingCashbacks.put(transactionId, event);
        }

        return transactionId;
    }

    /**
     * NÍVEL 3 — REFUND
     * Estorna um pagamento feito anteriormente e cancela o cashback agendado (caso ainda não tenha sido pago).
     */
    public boolean refund(String accountId, String transactionId, long timestamp) {
        processPendingCashbacks(timestamp);

        Transaction transaction = transactions.get(transactionId);
        if (transaction == null || transaction.isRefunded()) {
            return false;
        }

        if (!transaction.getAccountId().equals(accountId)) {
            return false;
        }

        Account account = getAccountById(accountId);
        if (account == null) {
            return false;
        }

        // Marcar como reembolsado
        transaction.setRefunded(true);

        // Devolve o saldo e reduz o totalSpent da conta
        account.refund(transaction.getAmount(), transaction.getTimestamp());

        // Se havia cashback agendado ainda pendente, cancela o evento
        CashbackEvent pendingEvent = pendingCashbacks.remove(transactionId);
        if (pendingEvent != null) {
            pendingEvent.cancel();
        }

        return true;
    }

    /**
     * NÍVEL 3 — SPENT IN WINDOW
     * Retorna o total gasto em pagamentos e transferências na janela [currentTimestamp - windowSizeMs, currentTimestamp].
     */
    public int spentInWindow(String accountId, long windowSizeMs, long currentTimestamp) {
        processPendingCashbacks(currentTimestamp);

        if (windowSizeMs < 0) {
            return 0;
        }

        Account account = getAccountById(accountId);
        if (account == null) {
            return 0;
        }

        long startTimestamp = currentTimestamp - windowSizeMs;
        return account.getSpentInWindow(startTimestamp, currentTimestamp);
    }

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
