# Level 3 Specification

## Commands

### `PAYMENT_WITH_CASHBACK <accountId> <amount> <timestamp> <cashbackPercent>`

- **Description:** Processes a payment with a cashback percentage.
- **Success:**
    - A unique transaction ID is generated and returned.
    - The `amount` is withdrawn from the account's balance.
    - A cashback event is scheduled to be credited to the account after 24 hours (86,400,000 milliseconds).
- **Failure:**
    - Returns `null` if:
        - The account does not exist.
        - The account has an insufficient balance.
- **Constraints:**
    - `amount` must be greater than 0.
    - `cashbackPercent` must be between 0 and 100 (inclusive).

### `REFUND <accountId> <transactionId> <timestamp>`

- **Description:** Refunds a previous transaction.
- **Success:**
    - Returns `true` if the refund is successful.
    - The transaction `amount` is returned to the account's balance.
    - The transaction `amount` is deducted from the account's `totalSpent`.
    - Any pending cashback associated with the transaction is canceled.
- **Failure:**
    - Returns `false` if:
        - The `transactionId` does not exist.
        - The transaction has already been refunded.
        - The `accountId` does not match the account of the original transaction.

### `SPENT_IN_WINDOW <accountId> <windowSizeMs> <currentTimestamp>`

- **Description:** Calculates the total amount spent by an account within a sliding time window.
- **Success:**
    - Returns the sum of all payments made within the time interval `[currentTimestamp - windowSizeMs, currentTimestamp]`.
- **Failure:**
    - Returns `0` if the account does not exist.
- **Constraints:**
    - `windowSizeMs` must be greater than or equal to 0.
