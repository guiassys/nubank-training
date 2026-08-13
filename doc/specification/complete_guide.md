# Roteiro Detalhado de Implementação para CodeSignal

Este roteiro foi projetado para uma implementação incremental e rápida, focando em passar nos testes de cada nível antes de avançar para o próximo.

---

### **Preparação (Antes de começar)**

1.  **Crie as Classes de Exceção:** A plataforma de testes espera exceções customizadas. Comece criando todas elas para não ter que interromper o fluxo depois.
    *   `AccountAlreadyExistsException`
    *   `AccountNotFoundException`
    *   `InsufficientBalanceException`
    *   `InvalidAmountException`
    *   `TransactionNotFoundException`

2.  **Estrutura das Classes Principais:** Crie os arquivos `Account.java`, `WalletService.java`, e as interfaces, mas deixe os métodos vazios. Isso organiza o trabalho.

---

### **Nível 1: Funcionalidades Básicas**

O objetivo é ter um sistema de contas funcional para criar contas, depositar, transferir e consultar saldos.

1.  **Classe `Account` (Essencial):**
    *   Adicione os campos: `String id`, `int balance`.
    *   Implemente o construtor: `public Account(String accountId)`.
    *   Implemente `deposit(int amount)`: Apenas incrementa o saldo.
    *   Implemente `withdraw(int amount)`: Decrementa o saldo somente se `balance >= amount`. Retorne `true` ou `false`.
    *   Implemente os getters para `id` e `balance`.

2.  **Classe `WalletService` (Nível 1):**
    *   Adicione a estrutura de dados principal: `private final Map<String, Account> accounts = new ConcurrentHashMap<>();`.
    *   **`create(String accountId)`:**
        *   Verifique se a conta já existe com `accounts.containsKey()`. Se sim, lance `AccountAlreadyExistsException`.
        *   Crie e adicione a nova conta: `accounts.putIfAbsent(accountId, new Account(accountId))`.
    *   **`getAccountById(String accountId)`:**
        *   Busque a conta no mapa. Se não encontrar, lance `AccountNotFoundException`.
        *   Retorne a conta. (Este será um método auxiliar muito útil).
    *   **`deposit(String accountId, int amount)`:**
        *   Use `getAccountById` para encontrar a conta.
        *   Chame `account.deposit(amount)`.
        *   Retorne o novo saldo.
    *   **`balance(String accountId)`:**
        *   Use `getAccountById` e retorne `account.getBalance()`.
    *   **`transfer(String from, String to, int amount)`:**
        *   Valide a entrada: `from` não pode ser igual a `to` e `amount` deve ser positivo (lance `IllegalArgumentException`).
        *   Use `getAccountById` para obter as duas contas.
        *   **Ponto Crítico (Concorrência):** Implemente o **bloqueio ordenado** para evitar deadlock desde o início.
            *   Determine a ordem de bloqueio comparando os IDs: `Object lock1 = from.compareTo(to) < 0 ? fromAccount : toAccount;`.
            *   Sincronize em `lock1` e depois em `lock2`.
            *   Dentro dos blocos `synchronized`, chame `fromAccount.withdraw(amount)`.
            *   Se o saque falhar, lance `InsufficientBalanceException`.
            *   Se o saque funcionar, chame `toAccount.deposit(amount)`.

---

### **Nível 2: Relatórios e Lógica de Pagamento**

O foco é rastrear gastos e implementar funcionalidades de relatório.

1.  **Classe `Account` (Nível 2):**
    *   Adicione o campo: `private int totalSpent = 0;`.
    *   Modifique `withdraw(int amount)` para que, em caso de sucesso, ele some o valor a `totalSpent`.
    *   Adicione o getter `getTotalSpent()`.

2.  **Classe `WalletService` (Nível 2):**
    *   **`payment(String accountId, int amount, long timestamp)`:**
        *   Valide se `amount > 0`.
        *   Use `getAccountById` para obter a conta.
        *   Chame `account.withdraw(amount, timestamp)` (você precisará criar essa nova versão do método `withdraw` na classe `Account`).
    *   **`topSpenders(int k)`:**
        *   Se `k <= 0`, retorne uma lista vazia.
        *   Crie o `Comparator<Account>` para a heap (Min-Heap): `(a1, a2) -> Integer.compare(a1.getTotalSpent(), a2.getTotalSpent())`.
        *   Crie a `PriorityQueue<Account> minHeap`.
        *   Itere sobre `accounts.values()`. Para cada conta com `totalSpent > 0`:
            *   Adicione à heap: `minHeap.offer(account)`.
            *   Se `minHeap.size() > k`, remova o menor: `minHeap.poll()`.
        *   Após o loop, extraia os elementos da heap para uma lista, inverta a ordem e formate a string de saída.

---

### **Nível 3: Lógica Temporal e Cashback**

Adiciona complexidade com eventos que dependem do tempo.

1.  **Crie as Classes de Modelo:**
    *   `Transaction.java`: com `transactionId`, `accountId`, `amount`, `timestamp`, `cashbackAmount`, e um booleano `refunded`.
    *   `CashbackEvent.java`: com `transactionId`, `accountId`, `amount`, `maturityTimestamp` e um booleano `cancelled`. Implemente `Comparable` para ordenar por `maturityTimestamp`.

2.  **Classe `WalletService` (Nível 3):**
    *   Adicione as novas estruturas de dados:
        *   `Map<String, Transaction> transactions`
        *   `PriorityBlockingQueue<CashbackEvent> cashbackQueue`
        *   `Map<String, CashbackEvent> pendingCashbacks`
    *   **`processPendingCashbacks(long currentTimestamp)` (Método Auxiliar):**
        *   Crie um loop `while` que processa eventos da `cashbackQueue` cujo `maturityTimestamp <= currentTimestamp`.
        *   Dentro do loop, remova o evento do mapa `pendingCashbacks`.
        *   Se o evento não foi cancelado, deposite o valor do cashback na conta correspondente.
    *   **`paymentWithCashback(...)`:**
        *   Chame `processPendingCashbacks`.
        *   Valide os inputs.
        *   Use `getAccountById` e chame `account.withdraw()`.
        *   Crie o objeto `Transaction` e armazene-o.
        *   Se `cashbackAmount > 0`, crie o `CashbackEvent`, adicione-o à `cashbackQueue` e ao mapa `pendingCashbacks`.
        *   Retorne o `transactionId`.
    *   **`refund(String accountId, String transactionId, long timestamp)`:**
        *   Chame `processPendingCashbacks`.
        *   Busque a transação. Se não existir, for de outra conta ou já tiver sido estornada, lance `TransactionNotFoundException`.
        *   Marque a transação como `refunded`.
        *   Chame um novo método `account.refund(amount, timestamp)` para devolver o dinheiro e ajustar os gastos.
        *   Cancele o cashback: remova do `pendingCashbacks` e chame `event.cancel()`.
    *   **`spentInWindow(...)`:**
        *   Chame `processPendingCashbacks`.
        *   Use `getAccountById` e delegue a lógica para um novo método `account.getSpentInWindow(...)`.

3.  **Classe `Account` (Nível 3):**
    *   Adicione `private final NavigableMap<Long, Integer> spendingHistory = new ConcurrentSkipListMap<>();` para o `spentInWindow`.
    *   Modifique `withdraw` para registrar o gasto no `spendingHistory`.
    *   Implemente `getSpentInWindow(start, end)` usando `subMap` e somando os valores.
    *   Implemente `refund(amount, timestamp)` para remover/ajustar o gasto no `spendingHistory` e decrementar `totalSpent`.

---

### **Nível 4: Segurança e Limites**

Finaliza a implementação com regras de negócio de segurança.

1.  **Classe `Account` (Nível 4):**
    *   Adicione os campos: `boolean locked`, `dailyLimit`, `dailySpent` (mapa), e `debitTimestamps` (fila).
    *   **Modifique `withdraw` (Lógica Principal):**
        *   Verifique se a conta está bloqueada (`isLocked`).
        *   Implemente a **detecção de fraude**: limpe timestamps antigos na `debitTimestamps` e verifique se há muitas transações recentes. Se sim, bloqueie a conta.
        *   Implemente a **verificação de limite diário**: verifique se o gasto do dia (`dailySpent`) mais o `amount` atual excede o `dailyLimit`.
        *   Se todas as verificações passarem, prossiga com o saque.
    *   Implemente `setDailyLimit(int limit)`.
    *   Implemente `unlock()`.

2.  **Classe `WalletService` (Nível 4):**
    *   **`setDailyLimit(String accountId, int limit)`:**
        *   Use `getAccountById` e chame `account.setDailyLimit(limit)`.
    *   **`unblock(String accountId)`:**
        *   Use `getAccountById` e chame `account.unlock()`.

Parabéns! Seguindo esses passos, você terá uma implementação robusta e correta, nível a nível.
