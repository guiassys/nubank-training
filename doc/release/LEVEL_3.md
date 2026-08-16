# Release Notes - Level 3

## Summary of Level 3

Level 3 adds advanced financial mechanisms, such as scheduled transactions, refunds, and queries over sliding time windows.

### Implemented Features:

- **`PAYMENT_WITH_CASHBACK <accountId> <amount> <timestamp> <cashbackPercent>`**: Executes a payment and schedules a cashback to be credited to the user's account after 24 hours.
- **`REFUND <accountId> <transactionId> <timestamp>`**: Refunds a payment, returning the amount to the user's balance and canceling any associated pending cashback.
- **`SPENT_IN_WINDOW <accountId> <windowSizeMs> <currentTimestamp>`**: Calculates the total spending of an account within a sliding time window.

---

## Learnings & Notes — Level 3

| Topic | Notes / Reflections |
| :----- | :-------------------- |
| **Data Structures** | **`TreeMap<Long, Integer>`**: Used in `Account` to maintain the spending timeline and perform time window queries (`.subMap()`) in $O(\log N)$.<br>**`PriorityQueue<CashbackEvent>`**: Min-Heap used to simulate the cashback settlement queue ordered by time.<br>**`Map<String, Transaction>` / `Map<String, CashbackEvent>`**: ID mapping to allow $O(1)$ lookup for refunds and event cancellations. |
| **Complexity** | **`paymentWithCashback()`**: $O(\log E)$ (insertion into the event Heap).<br>**`refund()`**: $O(1)$ for event cancellation + $O(\log T)$ for adjusting the transaction tree.<br>**`spentInWindow()`**: $O(\log T + V)$ where $T$ is the total number of transactions and $V$ is the number of transactions within the window. |
| **Design Decisions** | **Future Event Processing**: Implemented a "lazy processing" mechanism for cashbacks. Before executing any operation, the system checks and processes any pending cashbacks that have reached their maturity time.<br>**Integer Calculation**: Cashback calculation uses `(amount * cashbackPercent) / 100` to avoid floating-point precision errors. |
| **Edge Case Handling**| - Cancellation of pending cashback via an `isCancelled()` flag if a `REFUND` occurs before maturity.<br>- Support for multiple payments at the same `timestamp` using `spendingHistory.merge(timestamp, amount, Integer::sum)`. |