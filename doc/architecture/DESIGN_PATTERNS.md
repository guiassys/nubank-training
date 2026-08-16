# Used Design Patterns

This document summarizes the main design and concurrency patterns applied in the development of the digital wallet system.

| Pattern Name | Usage Description |
| :--- | :--- |
| **Facade** | The `IWalletService` interface acts as a facade, providing a simplified and unified interface that orchestrates system operations (account creation, transfers, etc.). It hides the internal complexity of managing multiple data structures and business logic. |
| **Interface Segregation Principle (ISP)** | Wallet features were divided into smaller, more cohesive interfaces (`IAccountAdminService`, `IAccountFinancialService`, `ITransactionService`, `IReportingService`). This prevents client classes from depending on methods they do not use, promoting a cleaner and decoupled design. |
| **Singleton** | The `WalletService` class is implemented as an implicit Singleton within the application context. This ensures that only a single instance of the service exists to manage the state of all accounts and transactions, avoiding inconsistencies. |
| **Repository** | The `WalletService` class manages the collection of domain entities (accounts and transactions), abstracting the data source (in this case, in-memory maps) and centralizing object access and persistence. |
| **Strategy** | `MIN_HEAP_SPENDER_COMPARATOR` is an implementation of the Strategy pattern. It encapsulates the comparison algorithm used in the priority queue to determine top spenders, allowing the sorting logic to be defined and swapped independently. |
| **State** | `Account` utilizes the State pattern to manage the "frozen/locked" state. This allows the object's behavior to change dynamically (preventing debits) without scattering conditional (`if/else`) checks throughout the code that interacts with the account. |
| **Command** | `CashbackEvent` operates as a command object. It encapsulates all necessary information to execute an action (applying cashback) at a future point in time. The `cashbackQueue` acts as a scheduler, processing these commands once they mature. |
| **Monitor Object** | `Account` acts as a monitor object, using `synchronized` blocks to protect its internal state (balance, spending history) in a concurrent environment. This ensures that only one thread can modify its data at a time, preventing race conditions. |
| **Ordered Locking** | In the `transfer` operation, the ordered locking pattern is used to prevent deadlocks. By acquiring locks on the source and target accounts always in the same sequence (based on account ID), the possibility of a lethal circular wait condition between threads is eliminated. |