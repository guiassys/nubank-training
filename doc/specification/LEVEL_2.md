# Level 2 Specification

## Commands

### `PAYMENT <accountId> <amount> <timestamp>`

- **Description:** Records a payment of `amount` from the account at a specific `timestamp`.
- **Success:**
    - The `amount` is withdrawn from the account's balance.
    - The `amount` is added to the account's total spent.
- **Failure:**
    - The payment is not recorded if the account has an insufficient balance.
- **Constraints:**
    - The `amount` must be greater than zero.

### `TOP_SPENDERS <k>`

- **Description:** Returns a list of the top `k` spenders.
- **Output:**
    - A list of strings, where each string is in the format `"accountId(totalSpent)"`.
    - The list is sorted in descending order of `totalSpent`.
- **Tie-breaking:**
    - If two accounts have the same `totalSpent`, they are sorted by `accountId` in alphabetical order.
- **Edge Cases:**
    - If `k` is less than or equal to 0, an empty list is returned.
    - Accounts with `totalSpent` of 0 are not included in the list.
    - If `k` is greater than the number of accounts with spending, all accounts with spending are returned.
