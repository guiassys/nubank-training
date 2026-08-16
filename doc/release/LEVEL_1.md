# Release Notes - Level 1

## Summary of Level 1

Level 1 introduces the core of the digital wallet system, allowing account creation and basic financial operations.

### Implemented Features:

- **CREATE `<accountId>`**: Creates a new account with an initial balance of zero.
- **DEPOSIT `<accountId> <amount>`**: Adds an amount to an existing account's balance.
- **TRANSFER `<from> <to> <amount>`**: Transfers an amount between two accounts, validating the existence of both and the source account's balance.
- **BALANCE `<accountId>`**: Checks the balance of an account.

---

## Learnings & Notes — Level 1

| Topic | Notes / Reflections |
| :----- | :-------------------- |
| **Data Structures** | **`HashMap<String, model.Account>`** was chosen to store accounts because `accountId` is a **unique key** and will be used frequently to locate an account. This structure offers lookup, insertion, and removal with expected **O(1)** complexity. |
| **Complexity** | **`create()`**: **O(1)** time, **O(1)** space. **`deposit()`**: **O(1)** time, **O(1)** space. **`balance()`**: **O(1)** time, **O(1)** space. **`transfer()`**: **O(1)** time. Total service space complexity is **O(n)**, where `n` is the number of accounts. |
| **Design Decisions** | The project was separated into two responsibilities: **`service.WalletService`** (coordinates use cases) and **`model.Account`** (represents the domain entity and protects its state). |
| **Object-Oriented Principles** | The internal state of an account (`balance`) is mutated exclusively through its own methods (`deposit()` and `withdraw()`), ensuring the entity protects its own **invariants**. |
| **Encapsulation** | The responsibility of modifying the balance belongs to the **`model.Account`** class. `service.WalletService` does not directly alter the `balance` attribute, reducing coupling. |
| **Error Handling** | Missing accounts are handled as part of the normal business flow, returning failure values. Invalid input values (`amount <= 0`) are handled using `IllegalArgumentException`. |
| **Atomicity** | The concept of atomicity in transfers was identified as an area to explore in future levels, ensuring the operation completes entirely or is fully rolled back upon failure. |
| **Java Best Practices** | Program to interfaces (`Map` instead of `HashMap`), use `final` for immutable attributes, and maintain clear, well-defined responsibilities. |