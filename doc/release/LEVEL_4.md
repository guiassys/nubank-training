# Release Notes - Level 4

## Summary of Level 4

Level 4 hardens the system for a production environment, introducing **concurrency safety** and laying the groundwork for complex business rules. This serves as the foundation for a reliable and scalable financial system.

### Implemented Improvements (Step 1):

- **Thread Safety in `model.Account`**: All methods that access or modify the account state (balance, spending history) were synchronized, ensuring that operations on a single account are atomic and consistent.
- **Concurrent Collections and Granular Locks in `service.WalletService`**: Core data structures (`HashMap`, `PriorityQueue`) were replaced with their concurrent counterparts (`ConcurrentHashMap`, `PriorityBlockingQueue`). Global synchronization on service methods was removed to eliminate lock contention bottlenecks, restricting isolated synchronization solely to the `cashbackQueue` block.
- **Deadlock Prevention in Transfers**: An ordered locking mechanism was implemented in the `transfer` method, which acquires locks on resources (accounts) in a consistent (lexicographical) order to prevent deadlock risks during concurrent transfers.

---

## Learnings & Notes — Level 4 (Step 1)

| Topic | Notes / Reflections |
| :----- | :-------------------- |
| **Concurrency vs. Performance** | Removing `synchronized` from public `WalletService` methods prevents requests for distinct accounts from queuing up unnecessarily. Combining concurrent collections with granular locks (on the `cashbackQueue` and within the `Account` object itself) maximizes concurrent throughput while maintaining consistency. |
| **Deadlock Prevention** | The ordered locking pattern is a classic and essential technique for avoiding deadlocks. By always acquiring locks in the same sequence (in this case, by the lexicographical order of the `accountId`), we guarantee that a lethal circular wait condition between two or more threads can never occur. |
| **Atomicity** | Using `ConcurrentHashMap.putIfAbsent()` for account creation demonstrates how leveraging atomic operations provided by concurrent collections simplifies code while ensuring correctness. |
| **Design for Concurrency** | Decoupling concerns by making the `Account` entity responsible for its own internal consistency (synchronizing its methods) while letting `WalletService` orchestrate multi-account operations creates a clear separation of concerns, making concurrent code easier to maintain and reason about. |