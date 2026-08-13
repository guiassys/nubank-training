# Level 4 Specification

## Theme: Concurrency, Security, and Limits

Level 4 raises the complexity to a senior level, introducing requirements for concurrency, fraud detection, and advanced business rules. The system must now be robust enough to handle multiple simultaneous operations without corrupting the state, while also applying security policies and usage limits.

---

## Non-Functional Requirement: Concurrency

All `WalletService` operations must be **thread-safe** and support high throughput without global service-level bottlenecks. Multiple concurrent calls to the service's methods (e.g., `TRANSFER`, `PAYMENT`, `DEPOSIT`) must not result in race conditions or inconsistent states.

- **Example Scenario:** If two threads attempt to transfer funds from the same account (`A`) simultaneously, the final balance of `A` must be the correct and deterministic result of the two operations, without one improperly overwriting the other.

---

## New Features and Behaviors

### 1. Automatic Fraud Lock

The system must implement a simple fraud detection rule to lock accounts.

- **Rule:** An account is considered suspicious and must be **automatically locked** if it performs more than **3 debit transactions** (`PAYMENT` or `TRANSFER` from it) within a **2-minute** interval (120,000 ms).
- **Behavior of Existing Commands for Locked Accounts:**
  - `PAYMENT`, `TRANSFER`, `PAYMENT_WITH_CASHBACK`: Must fail (return `false`, `null` or throw `AccountLockedException`) if the source account is locked.
  - `DEPOSIT`: Must continue to function normally.
  - `BALANCE`: Must continue to function normally.
- The transaction counter for fraud detection must be reset whenever the lock is activated or removed.

### 2. Daily Spending Limit

Accounts can now have a daily spending limit.

- **New Command: `SET_DAILY_LIMIT <accountId> <limit>`**
  - **Description:** Sets a maximum spending limit (`amount`) that can be debited from an account per day.
  - **Success:** Returns `true` if the limit is set.
  - **Failure:** Returns `false` or throws `AccountNotFoundException` if the account does not exist.
  - **Note:** A "day" is defined by 24-hour periods starting from timestamp `0`. For example, day 1 is from `t=0` to `t=86399999`, day 2 is from `t=86400000` to `t=172799999`, and so on.

- **Behavior of Debit Commands:**
  - `PAYMENT`, `TRANSFER`, `PAYMENT_WITH_CASHBACK`: Before executing the operation, the service must check if the sum of debits already made on the current day plus the `amount` of the new transaction **exceeds the daily limit**.
  - If the limit is exceeded, the operation must fail (return `false`, `null` or throw `DailyLimitExceededException`).

### 3. Management of Locked Accounts

- **New Command: `UNBLOCK <accountId>`**
  - **Description:** Removes the "locked" status from an account, allowing it to perform debit transactions again.
  - **Success:** Returns `true` if the account was unlocked.
  - **Failure:** Returns `false` if the account does not exist or is not locked.