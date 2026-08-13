# Roteiro Resumido de Implementação para CodeSignal

Foco: Passar nos testes de cada nível de forma rápida e eficiente.

---

### **Nível 1: Core Banking**

1.  **`Account.java`:**
    *   Atributos: `id`, `balance`.
    *   Métodos: `deposit(amount)`, `withdraw(amount)`, getters.
2.  **`WalletService.java`:**
    *   Estrutura: `Map<String, Account> accounts`.
    *   `create(accountId)`: Checa duplicidade e adiciona no mapa. Lança `AccountAlreadyExistsException`.
    *   `deposit(accountId, amount)`: Encontra a conta e chama `account.deposit()`.
    *   `balance(accountId)`: Encontra a conta e retorna o saldo.
    *   `transfer(from, to, amount)`:
        *   Valida inputs.
        *   **Implementa bloqueio ordenado (`synchronized`)** para evitar deadlock.
        *   Chama `from.withdraw()` e `to.deposit()`. Lança exceções (`InsufficientBalanceException`, `AccountNotFoundException`).

---

### **Nível 2: Relatórios**

1.  **`Account.java`:**
    *   Adiciona `totalSpent`.
    *   Atualiza `totalSpent` no método `withdraw()`.
2.  **`WalletService.java`:**
    *   `payment(accountId, amount, timestamp)`: Delega para um novo `account.withdraw(amount, timestamp)`.
    *   `topSpenders(k)`:
        *   Usa uma `PriorityQueue` (Min-Heap) de tamanho `k`.
        *   Itera nas contas, mantendo os `k` maiores gastadores na heap.
        *   Extrai, inverte e formata o resultado.

---

### **Nível 3: Lógica Temporal**

1.  **Novas Classes:**
    *   `Transaction.java`: `id`, `accountId`, `amount`, `timestamp`, `cashbackAmount`, `refunded`.
    *   `CashbackEvent.java`: `txId`, `accountId`, `amount`, `maturityTimestamp`, `cancelled`. Implementa `Comparable`.
2.  **`WalletService.java`:**
    *   **Estruturas:** `Map<String, Transaction>`, `PriorityBlockingQueue<CashbackEvent>`, `Map<String, CashbackEvent>`.
    *   **`processPendingCashbacks(timestamp)`:** Método privado para processar cashbacks maduros da fila.
    *   `paymentWithCashback(...)`:
        *   Chama `processPendingCashbacks`.
        *   Realiza o pagamento.
        *   Cria `Transaction` e `CashbackEvent`, e os armazena nas estruturas de dados.
    *   `refund(...)`:
        *   Chama `processPendingCashbacks`.
        *   Encontra a transação, marca como estornada.
        *   Devolve o dinheiro para a conta (`account.refund()`).
        *   Cancela o `CashbackEvent` pendente.
    *   `spentInWindow(...)`: Delega para `account.getSpentInWindow()`.
3.  **`Account.java`:**
    *   Adiciona `NavigableMap<Long, Integer> spendingHistory`.
    *   Implementa `getSpentInWindow()` com `subMap`.
    *   Implementa `refund()` para ajustar `totalSpent` e `spendingHistory`.

---

### **Nível 4: Segurança e Limites**

1.  **`Account.java`:**
    *   **Atributos:** `locked`, `dailyLimit`, `dailySpent` (mapa), `debitTimestamps` (fila).
    *   **Atualiza `withdraw()`:** Adiciona, no início do método, as seguintes verificações em ordem:
        1.  **Conta bloqueada?** (`isLocked`).
        2.  **Risco de fraude?** (Muitos débitos em janela curta).
        3.  **Limite diário excedido?**
    *   **Métodos:** `setDailyLimit()`, `unlock()`.
2.  **`WalletService.java`:**
    *   `setDailyLimit(accountId, limit)`: Delega para `account.setDailyLimit()`.
    *   `unblock(accountId)`: Delega para `account.unlock()`.

---
**Lembrete:** Crie todas as classes de exceção customizadas no início para agilizar o processo.
