# Nubank Training

> **Objetivo:** Projeto voltado para preparação em processos seletivos técnicos, focado em algoritmos, estruturas de dados, lógica de negócio e desenvolvimento Java (estilo CodeSignal).

---

## Ambiente de Desenvolvimento

| Componente | Especificação |
| :--- | :--- |
| **Java** | 21 |
| **Maven** | 3.8.7 |
| **IDE** | IntelliJ IDEA |
| **SO** | Ubuntu (WSL2) |

---

## Simulado 01 — Sistema de Carteiras Digitais

O projeto simula o núcleo de um sistema de carteiras digitais, implementado de forma incremental em três níveis.

---

### 🟢 Nível 1 — Operações Básicas

O Nível 1 introduz o núcleo do sistema, permitindo a criação de contas e a realização de operações financeiras básicas.

| Comando | Sintaxe | Descrição |
| :--- | :--- | :--- |
| `CREATE` | `CREATE <accountId>` | Cria uma nova conta com saldo inicial zero. |
| `DEPOSIT` | `DEPOSIT <accountId> <amount>` | Adiciona um valor ao saldo de uma conta. |
| `TRANSFER` | `TRANSFER <from> <to> <amount>` | Transfere um valor entre duas contas. |
| `BALANCE` | `BALANCE <accountId>` | Consulta o saldo de uma conta. |

> Para mais detalhes sobre a implementação, consulte o [Release Notes do Nível 1](./doc/release/LEVEL_1.md).

---

### 🟡 Nível 2 — Agregadores, Histórico e Métricas (Top K)

O Nível 2 expande o sistema com inteligência financeira, introduzindo transações temporais e relatórios de gastos.

| Comando | Sintaxe | Descrição |
| :--- | :--- | :--- |
| `PAYMENT` | `PAYMENT <accountId> <amount> <timestamp>` | Executa um pagamento, debitando o valor do saldo e contabilizando-o no total de gastos. |
| `TOP_SPENDERS` | `TOP_SPENDERS <k>` | Retorna os `k` usuários que mais gastaram no sistema. |

> Para mais detalhes sobre a implementação, consulte o [Release Notes do Nível 2](./doc/release/LEVEL_2.md).

---

### 🟠 Nível 3 — Operações Temporais Avançadas

O Nível 3 adiciona mecanismos financeiros avançados, como cashbacks, estornos e consultas em janelas de tempo.

| Comando | Sintaxe | Descrição |
| :--- | :--- | :--- |
| `PAYMENT_WITH_CASHBACK` | `PAYMENT_WITH_CASHBACK <accountId> <amount> <timestamp> <cashbackPercent>` | Realiza um pagamento e agenda um cashback para ser creditado após 24 horas. |
| `REFUND` | `REFUND <accountId> <transactionId> <timestamp>` | Estorna um pagamento e cancela qualquer cashback pendente associado. |
| `SPENT_IN_WINDOW` | `SPENT_IN_WINDOW <accountId> <windowSizeMs> <currentTimestamp>` | Calcula o total de gastos de uma conta em uma janela de tempo deslizante. |

> Para mais detalhes sobre a implementação, consulte o [Release Notes do Nível 3](./doc/release/LEVEL_3.md).

---

### 🔵 Nível 4 — Concorrência e Robustez

O Nível 4 robustece o sistema para um ambiente de produção, introduzindo **segurança contra concorrência** e preparando o terreno para regras de negócio complexas.

**Melhorias Implementadas:**
- **Thread Safety** em `model.Account`.
- **Coleções Concorrentes e Locks Granulares** em `service.WalletService`.
- **Prevenção de Deadlock** em transferências com *ordered locking*.

> Para mais detalhes sobre a implementação, consulte o [Release Notes do Nível 4](./doc/release/LEVEL_4.md).
