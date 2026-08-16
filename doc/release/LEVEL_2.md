# Release Notes - Level 2

## Summary of Level 2

Level 2 expands the system with financial intelligence, introducing time-based transactions and the ability to generate high-performance reports on user spending.

### Implemented Features:

- **`PAYMENT <accountId> <amount> <timestamp>`**: Executes a payment from an account at a specific point in time, deducting the amount from the balance and recording it toward the user's total spending.
- **`TOP_SPENDERS <k>`**: Returns a list of the top `k` users who spent the most in the system. In the event of a tie in the spent amount, the tie-breaker criteria is the alphabetical order of the `accountId`.

---

## Learnings & Notes — Level 2

| Topic | Notes / Reflections |
| :----- | :-------------------- |
| **Data Structures** | Used a `PriorityQueue` (Min-Heap) capped at size $K$ for the `TOP_SPENDERS` feature. The root of the heap stores the "worst" candidate in the Top K, allowing efficient eviction. |
| **Complexity** | **`payment()`**: **$O(1)$** time and **$O(1)$** space.<br>**`topSpenders(k)`**: **$O(N \log K)$** time (where $N$ is the total number of accounts with spending) and **$O(K)$** auxiliary space for the `PriorityQueue`. |
| **Design Decisions** | - The Min-Heap `Comparator` was extracted into a `private static final` constant, improving performance and readability.<br>- The `totalSpent` attribute was encapsulated within the `Account` entity and is automatically updated on every `withdraw()` call. |
| **Comparators & Ties** | For the Min-Heap, when amounts spent are equal, the account with the lexicographically **greater** ID is considered "worse" for internal heap ordering purposes. The correct final order is restored at the end of the process. |
| **Edge Case Handling**| - Validation for $K \le 0$ returning an empty list.<br>- Filtering to ignore accounts with `totalSpent <= 0`.<br>- Gracefully handling scenarios where $K$ is larger than the total number of accounts with spending. |