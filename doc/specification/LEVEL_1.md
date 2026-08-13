# Level 1 Specification

## Commands

### `CREATE <accountId>`

- **Description:** Creates a new account with the given `accountId`.
- **Initial State:** The account is created with a balance of 0.
- **Success:** Returns `true` if the account is created successfully.
- **Failure:**
    - Returns `false` if an account with the same `accountId` already exists.

### `DEPOSIT <accountId> <amount>`

- **Description:** Deposits the specified `amount` into the account.
- **Success:**
    - Returns the new balance of the account.
- **Failure:**
    - Returns `-1` if the account with `accountId` does not exist.
- **Constraints:**
    - Throws `IllegalArgumentException` if `amount` is not greater than zero.

### `TRANSFER <from> <to> <amount>`

- **Description:** Transfers `amount` from the `from` account to the `to` account.
- **Success:**
    - Returns `true` if the transfer is successful.
    - The `amount` is withdrawn from the `from` account and deposited into the `to` account.
- **Failure:**
    - Returns `false` if:
        - The `from` or `to` account does not exist.
        - The `from` and `to` accounts are the same.
        - The `from` account has an insufficient balance.
        - The `amount` is not greater than zero.
    - The state of the accounts should not be changed in case of failure.

### `BALANCE <accountId>`

- **Description:** Retrieves the balance of the specified account.
- **Success:**
    - Returns the current balance of the account.
- **Failure:**
    - Returns `-1` if the account with `accountId` does not exist.
