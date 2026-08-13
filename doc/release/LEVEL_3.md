# Release Notes - Nível 3

## Resumo do Nível 3

O Nível 3 adiciona mecanismos financeiros avançados, como transações com agendamento, estornos e consultas em janelas de tempo deslizantes.

### Funcionalidades Implementadas:

- **`PAYMENT_WITH_CASHBACK <accountId> <amount> <timestamp> <cashbackPercent>`**: Realiza um pagamento e agenda um cashback para ser creditado na conta do usuário após 24 horas.
- **`REFUND <accountId> <transactionId> <timestamp>`**: Estorna um pagamento, devolvendo o valor ao saldo do usuário e cancelando qualquer cashback pendente associado.
- **`SPENT_IN_WINDOW <accountId> <windowSizeMs> <currentTimestamp>`**: Calcula o total de gastos de uma conta dentro de uma janela de tempo deslizante.

---

## Aprendizados & Anotações — Nível 3

| Tópico | Anotações / Reflexões |
| :----- | :-------------------- |
| **Estruturas de Dados** | **`TreeMap<Long, Integer>`**: Utilizado em `Account` para manter a linha do tempo de gastos e realizar consultas por janela de tempo (`.subMap()`) em $O(\log N)$.<br>**`PriorityQueue<CashbackEvent>`**: Min-Heap para simular a fila de liquidação de cashbacks por ordem de tempo.<br>**`Map<String, Transaction>` / `Map<String, CashbackEvent>`**: Mapeamento de IDs para permitir estorno e cancelamento de eventos em $O(1)$. |
| **Complexidade** | **`paymentWithCashback()`**: $O(\log E)$ (inserção na Heap de eventos).<br>**`refund()`**: $O(1)$ para cancelamento de evento + $O(\log T)$ para reajuste na árvore de transações.<br>**`spentInWindow()`**: $O(\log T + V)$ onde $T$ é o total de transações e $V$ é o número de transações na janela. |
| **Decisões de Design** | **Processamento de Eventos Futuros**: Foi implementado um mecanismo de "lazy processing" para os cashbacks. Antes de cada operação, o sistema verifica e processa quaisquer cashbacks pendentes que já atingiram seu tempo de maturação.<br>**Cálculo com Inteiros**: O cálculo de cashback usa `(amount * cashbackPercent) / 100` para evitar erros de precisão com ponto flutuante. |
| **Tratamento de Casos de Borda**| - Cancelamento de cashback pendente via *flag* `isCancelled()` caso um `REFUND` ocorra antes da maturação.<br>- Suporte a múltiplos pagamentos no mesmo `timestamp` com `spendingHistory.merge(timestamp, amount, Integer::sum)`. |