# Mermaid diagrams

## Flow - create
```mermaid
flowchart TD
    Start([Início: create accountId]) --> CheckExists{"Conta já existe?\naccounts.containsKey(accountId)"}
    
    CheckExists -- Sim --> ThrowExists[Lança AccountAlreadyExistsException]
    CheckExists -- Não --> CreateAccount[Cria novo objeto Account]
    
    CreateAccount --> PutInMap["Adiciona no mapa 'accounts'\naccounts.putIfAbsent(accountId, account)"]
    PutInMap --> ReturnTrue[Retorna true]

    ThrowExists --> End([Fim])
    ReturnTrue --> End
```
---

## Flow - deposit
```mermaid
flowchart TD
    Start([Início: deposit accountId, amount]) --> GetAccount["Busca a conta\ngetAccountById(accountId)"]
    
    GetAccount --> CheckAccount{Conta existe?}
    CheckAccount -- Não --> ThrowNotFound[Lança AccountNotFoundException]
    CheckAccount -- Sim --> DepositAmount["Chama account.deposit(amount)"]
    
    DepositAmount --> GetBalance["Retorna o novo saldo\naccount.getBalance()"]
    
    ThrowNotFound --> End([Fim])
    GetBalance --> End
```
---

## Flow - transfer
```mermaid
flowchart TD
    Start([Início: transfer from, to, amount]) --> ValidateInput{from == to OU amount <= 0?}
    ValidateInput -- Sim --> ThrowException[Lança IllegalArgumentException]

    ValidateInput -- Não --> GetAccounts[Busca contas de origem e destino]
    GetAccounts --> CheckAccounts{Ambas existem?}
    CheckAccounts -- Não --> ThrowNotFound[Lança AccountNotFoundException]
    
    CheckAccounts -- Sim --> Withdraw["Tenta sacar da origem\nfromAccount.withdraw(amount)"]
    
    Withdraw --> CheckWithdraw{Saque bem-sucedido?}
    CheckWithdraw -- Não --> ThrowInsufficientBalance[Lança InsufficientBalanceException]
    
    CheckWithdraw -- Sim --> Deposit["Deposita na conta de destino\ntoAccount.deposit(amount)"]
    Deposit --> ReturnTrue[Retorna true]

    ThrowException --> End([Fim])
    ThrowNotFound --> End
    ThrowInsufficientBalance --> End
    ReturnTrue --> End
```
---

## Flow - balance (simples)
```mermaid
flowchart TD
    Start([Início: balance accountId]) --> GetAccount["Busca a conta\ngetAccountById(accountId)"]
    
    GetAccount --> CheckAccount{Conta existe?}
    CheckAccount -- Não --> ThrowNotFound[Lança AccountNotFoundException]
    CheckAccount -- Sim --> GetBalance["Retorna o saldo atual\naccount.getBalance()"]
    
    ThrowNotFound --> End([Fim])
    GetBalance --> End
```
---